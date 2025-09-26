package com.nanhang.lease.web.admin.service.impl;

import com.mysql.cj.util.TimeUtil;
import com.nanhang.lease.web.admin.service.LoginService;
import com.nanhang.lease.web.admin.vo.login.CaptchaVo;
import com.wf.captcha.SpecCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
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
}
