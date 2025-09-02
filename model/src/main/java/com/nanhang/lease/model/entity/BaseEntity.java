package com.nanhang.lease.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
//将共有属性放在一个类中，该类实现Serializable接口，其他类继承该类
@Data
public class BaseEntity implements Serializable {

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "创建时间")
    //TableField用于告诉mybatis-plus该字段是数据库中的字段
    //该注解有一个属性Fill,用来自动填充，我们需要设置自动填充的时机，这里设置的是插入时自动填充
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    @JsonIgnore//这个注解表示转换成Json的时候忽略该属性
    private Date createTime;

    @Schema(description = "更新时间")
    //TableField用于告诉mybatis-plus该字段是数据库中的字段
    //该注解有一个属性Fill,用来自动填充，我们需要设置自动填充的时机，这里设置的是修改时自动填充
    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    @JsonIgnore//这个注解表示转换成Json的时候忽略该属性
    private Date updateTime;

    @Schema(description = "逻辑删除")
    @TableField("is_deleted")
    @TableLogic//这个注解表示逻辑删除，默认0是未删除，1是删除，用于查询所有
    private Byte isDeleted;

}