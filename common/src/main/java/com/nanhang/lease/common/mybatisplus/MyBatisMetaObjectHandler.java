package com.nanhang.lease.common.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

//配置model.src.main.java.com.nanhang.lease.model.BaseEntity
// 中的MyBatisPlus注解自动添加的Fill添加的值
@Component
public class MyBatisMetaObjectHandler implements MetaObjectHandler {
    //配置createTime字段插入的内容（插入时机再实体类中已经使用注解设置了）
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
    }

    //配置updateTime字段插入的内容（插入时机再实体类中已经使用注解设置了）
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
    }
}