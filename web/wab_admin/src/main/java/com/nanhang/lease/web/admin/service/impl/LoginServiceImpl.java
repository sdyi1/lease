package com.nanhang.lease.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mysql.cj.util.TimeUtil;
import com.nanhang.lease.common.exception.LeaseException;
import com.nanhang.lease.common.result.ResultCodeEnum;
import com.nanhang.lease.common.utils.JwtUtil;
import com.nanhang.lease.model.entity.SystemUser;
import com.nanhang.lease.model.enums.BaseStatus;
import com.nanhang.lease.web.admin.mapper.SystemUserMapper;
import com.nanhang.lease.web.admin.mapper.UserInfoMapper;
import com.nanhang.lease.web.admin.service.LoginService;
import com.nanhang.lease.web.admin.vo.login.CaptchaVo;
import com.nanhang.lease.web.admin.vo.login.LoginVo;
import com.wf.captcha.SpecCaptcha;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SystemUserMapper systemUserMapper;

    @Override
    public CaptchaVo getCaptche() {

        //使用开源的验证码工具包
        //获取验证码图片
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
        // .text根据图片获取验证码，toLowerCase()转换为小写
        String lowerCase = specCaptcha.text().toLowerCase();
        //将验证码图片转换为base64编码
        String base64 = specCaptcha.toBase64();
        // 使用java自带的包 util的UUID下面的方法randomUUID生成验证码key，并且按照本项目的key格式 admin:login:key
        String key = "admin:login:"+ UUID.randomUUID();
        //将验证码键值对存储到Redis中，并且设置过期时间  60  单位秒（TimeUnit是java自带的枚举类）
        stringRedisTemplate.opsForValue().set(key,lowerCase,60, TimeUnit.SECONDS);


        return new CaptchaVo(base64,key);
    }

    @Override
    public String login(LoginVo loginVo) {
        //首先判断验证码是否为null
        if(loginVo.getCaptchaCode()==null){
            throw  new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_NOT_FOUND);
        }


        //先从Redis中获取验证码
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        String s = stringStringValueOperations.get(loginVo.getCaptchaKey());
        //判断redis中的验证码是否存在（不存在就表示过期，抛出异常）
        if(s==null){
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_NOT_FOUND);
        }
        //判断验证码是否正确
        //toLowerCase()将loginVo中的验证码转换为小写,因为我们在前面获取验证码的时候就将验证码转换为小写了，这个操作可以让用户输入的验证码忽略大小写
        if(!s.equals(loginVo.getCaptchaCode().toLowerCase())){
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_ERROR);
        }

        //判断用户是否存在

        SystemUser systemUser = systemUserMapper.selectOneByUserName(loginVo.getUsername());

        if (systemUser==null){
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_NOT_EXIST_ERROR);
        }

        //查看用户状态判断是否被禁用
        if(systemUser.getStatus()== BaseStatus.DISABLE){
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_DISABLED_ERROR);
        }

        //判断用户输入的密码是否正确,注意我们存入数据库的密码是通过加密的，我们要将用户输入的密码使用同一种方法加密，在作比较
        if(!systemUser.getPassword().equals(DigestUtils.md5Hex(loginVo.getPassword()))){
            System.out.println(DigestUtils.md2Hex(loginVo.getPassword())+"======================================================================");
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_ERROR);
        }
        //如果以上判断都没有问题，那么就生成token返回给前端
        String token = JwtUtil.createToken(systemUser.getId(), systemUser.getUsername());

        return token;
    }
}
