package com.nanhang.lease.common.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//创建javaBean，在里面创建和配置文件一一对应的属性，根据属性名来读取配置文件的数据
@Data
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    //根据属性名字来读取对应属性
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}
