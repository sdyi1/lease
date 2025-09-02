package com.nanhang.lease.web.admin.custom.config;


import com.nanhang.lease.web.admin.custom.converter.StringToBaseEnumConverterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//这个类是用来配置mvc的，其实Spring会自动配置，我们在StringToItemTypeConverter这个类上方添加了注解@Component不需要写这个配置类

//是的，您的理解是正确的。
// 在Spring Boot中，当您在StringToItemTypeConverter类上添加了@Component注解后，
// Spring Boot会自动将其注册为Converter，无需额外的配置类。


@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    /*@Autowired
    private StringToItemTypeConverter stringToItemTypeConverter;
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToItemTypeConverter);
    }
    */

    @Autowired
    private StringToBaseEnumConverterFactory stringToBaseEnumConverterFactory;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        //使用addConverterFactory方法添加转换器工厂
        registry.addConverterFactory(this.stringToBaseEnumConverterFactory);
    }
}

