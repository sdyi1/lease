package com.nanhang.lease.common.minio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
/*@EnableConfigurationProperties(MinioProperties.class)//指定要读取的类*/
@ConfigurationPropertiesScan("com.nanhang.lease.common.minio")//指定包，扫描该包下面的所有带@ConfigurationProperties注解的类
public class MinioConfiguration {

    //将配置文件的数据读取到这里
    /*方法1，使用@Value注解
    @Value("${minio.endpoint}")
    String endpoint;

    详情见印象笔记，javaSSM.SpringBoot.SpringBoot配置统一配置管理 笔记*/



//    方法二
    /*
    创建类读取配置文件的数据
    详情见印象笔记，javaSSM.SpringBoot.读取配置文件 笔记
     */

    //在实际情况下，我们的客户端的地址不同，所以我不能在代码中写死，我们选择将这些放在springBoot项目的配置文件中Application.yaml中
    /*String endpoint = "http://127.0.0.1:9000";
    String accessKey = "minioadmin";
    String secretKey = "minioadmin";*/


    @Autowired
    private MinioProperties minioProperties;
    @Bean
    public MinioClient minioClient(){
        //创建并且输出minio客户端
//        minio结合SpringBoot使用见印象笔记.JavaEE公寓实战.Minio.MinIo在java中的使用 笔记

        return MinioClient.builder().endpoint(minioProperties.getEndpoint())
                    .credentials(minioProperties.getAccessKey(),minioProperties.getSecretKey())
                    .build();
    }
}
