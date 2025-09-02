package com.nanhang.lease.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;


public enum ItemType implements BaseEnum {

    APARTMENT(1, "公寓"),

    ROOM(2, "房间");

/*这个泛型是LabelInfo实体类的一个属性，对应表中的一个字段*/
    /*当我们标注了@EnumValue注解时，插入的时候：mybatisPlus会插入注解标记的属性code
    *                            查询的时候：mybatisPlus会根据注解标记的属性code查询数据
    *
    *
    *如果没有@EnumValue注解，插入的时候：MyBatisPlus会使用默认的 EnumTypeHandler插入，插入数据库时会存储实例名称 "APARTMENT" 或 "ROOM" 字符串
    *                            查询的时候：mybatisPlus会根据枚举常量的名称查询数据
    *
    * 当我们标注了@JsonValue注解是，返回给前端的Json数据是我们标注的属性
    *
    * 
    * 如果没有@JsonValue注解，查询的时候：mybatisPlus查询出结果后返回的是枚举常量的名称,
    * */

    @EnumValue   // 标记code字段作为数据库存储的值，插入时
    @JsonValue  // 标记code字段作为json序列化和反序列化时使用的值，查询时会将查询出的数据返回标记的属性
    private Integer code;
    private String name;

    @Override
    public Integer getCode() {
        return this.code;
    }


    @Override
    public String getName() {
        return name;
    }

    ItemType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

}
