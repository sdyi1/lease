package com.nanhang.lease.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
    @TableField(value = "create_time")
    @JsonIgnore//这个注解表示转换成Json的时候忽略该属性
    private Date createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time")
    @JsonIgnore//这个注解表示转换成Json的时候忽略该属性
    private Date updateTime;

    @Schema(description = "逻辑删除")
    @TableField("is_deleted")
    @TableLogic//这个注解表示逻辑删除，默认0是未删除，1是删除，用于查询所有
    private Byte isDeleted;

}