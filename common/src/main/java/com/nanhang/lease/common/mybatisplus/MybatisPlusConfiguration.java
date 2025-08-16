package com.nanhang.lease.common.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.nanhang.lease.web.*.mapper")
public class MybatisPlusConfiguration {

}