package com.nanhang.lease.common.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author 徐
 * @description MybatisPlus配置类
 * @date 2025/8/13
 */
@Configuration
@MapperScan("com.nanhang.lease.web.*.mapper")
public class MybatisPlusConfiguration {

}