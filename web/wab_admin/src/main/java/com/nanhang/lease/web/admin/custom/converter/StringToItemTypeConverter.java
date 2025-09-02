package com.nanhang.lease.web.admin.custom.converter;

import com.nanhang.lease.model.enums.ItemType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
/*
写了通用转换器类，这个就可以不用了



//如果重写了多个转换器类，会根据前端传入的数据类型和RequestParam设置的目标数据类型来判断使用哪个转换器类

@Component
public class StringToItemTypeConverter implements Converter<String, ItemType> {
    @Override
    public ItemType convert(String source) {
        */
/*
            这个方法后期维护麻烦，代码臃肿
        if (ItemType.APARTMENT.getCode().toString().equals(source)){
            return ItemType.APARTMENT;
        } else if (ItemType.ROOM.getCode().toString().equals(source)) {
            return ItemType.ROOM;
        }
        *//*


//        使用泛型的方法
        //使用values获取泛型中的所有实例，遍历所有实例，判断是否有和前端传入的code相等的实例
        ItemType[] values = ItemType.values();
        for (ItemType value : values) {
            if(value.getCode()==Integer.valueOf(source)){
                return value;
            }
        }
        //如果传入的code泛型中没有对应的实体类就抛出异常
        throw  new IllegalArgumentException("code"+source+"非法");

        //重写转换器按理说添加了@Component注解Spring会自动注册，但是我们写了注册配置类
//        wab_admin/src/main/java/com/nanhang/lease/web/admin/custom/config/WebMvcConfiguration.java
    }
}





*/
