package com.nanhang.lease.web.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nanhang.lease.model.entity.SystemUser;
import com.nanhang.lease.web.admin.mapper.SystemUserMapper;
import com.nanhang.lease.web.admin.service.SystemUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nanhang.lease.web.admin.vo.system.user.SystemUserItemVo;
import com.nanhang.lease.web.admin.vo.system.user.SystemUserQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liubo
 * @description 针对表【system_user(员工信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser>
        implements SystemUserService {

    @Autowired
    protected SystemUserMapper systemUserMapper;

    //分页查询
    @Override
    public IPage<SystemUserItemVo> selectByPage(Page<SystemUserItemVo> page, SystemUserQueryVo queryVo) {

        return systemUserMapper.selectByPage(page,queryVo);
    }

    //根据id查询
    @Override
    public SystemUserItemVo getByIdDiy(Long id) {
        return systemUserMapper.getByIdDiy(id);
    }
}




