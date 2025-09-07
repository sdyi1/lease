package com.nanhang.lease.web.admin.service.impl;

import com.nanhang.lease.common.minio.MinioProperties;
import com.nanhang.lease.web.admin.service.FileService;
import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    //注入MinioClient使用它的方法
    // 方法详情见印象笔记JavaEE公寓实战.Minio.MinIo在java中的使用
    @Autowired
    private MinioClient minioClient;
//    Ctrl+b可以查看，我们在这个类里面读取了配置文件配置的关于Minio的基本配置 如账号密码
    @Autowired
    private MinioProperties properties;

    @Override
    public String upload(MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        //传入桶的名称，判断是否有该桶
        //如果没有就创建该通，设置权限，上传文件
        //如果有


            //传入桶的名称，判断是否有该桶
            boolean b = minioClient.bucketExists(BucketExistsArgs.builder().bucket(properties.getBucketName()).build());
            //如果没有就创建该通，设置权限，上传文件
            if (!b){
                //创建桶
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(properties.getBucketName())
                        .build());

                /*
minioClient.setBucketPolicy()：
        这是 MinIO 客户端的一个方法，用于设置存储桶的访问策略
        通过这个方法可以控制谁可以访问存储桶中的文件以及可以执行什么操作（如读取、写入等）
SetBucketPolicyArgs.builder()：
        这是构建 SetBucketPolicyArgs 对象的构建器模式
        builder() 是一个静态方法，返回一个 SetBucketPolicyArgs.Builder 构造器对象（这是一个专门用来构建 SetBucketPolicyArgs 实例的构建器），用于逐步构建参数
.bucket(properties.getBucketName())：
        设置要应用策略的存储桶名称
        properties.getBucketName() 从配置属性中获取存储桶名称
        这个方法将存储桶名称设置到构建器中
.config(createBucketPolicyConfig(properties.getBucketName()))：
        这是设置存储桶策略配置的方法
        createBucketPolicyConfig() 是一个自定义方法，用于生成策略配置的 JSON 字符串
        这个方法接收存储桶名称作为参数，返回一个 JSON 格式的策略配置
createBucketPolicyConfig() 方法：
        这是一个自定义方法，用于创建存储桶策略配置
        它返回一个 JSON 字符串，定义了存储桶的访问权限
        根据我看到的代码，这个策略允许任何人（"*"）执行 "s3:GetObject" 操作，即可以从存储桶中读取对象（文件）
        这样设置后，上传到该存储桶的文件可以通过 URL 直接访问
.build()：
        这是构建器模式的最后一步，用于创建最终的 SetBucketPolicyArgs 对象
        将之前通过各种 setter 方法设置的参数组合成一个完整的参数对象*/

                /*
                这里用的是典型的 Builder 模式实现，每次调用方法都返回返回构建器本身
                 */
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()//创建构造器对象
                        .bucket(properties.getBucketName())//调用构造器对象的bucket方法设置桶名称
                        .config(createBucketPolicyConfig(properties.getBucketName()))//调用构造器对象的config方法设置桶权限字符串，createBucketPolicyConfig方法输出我们配置的字符串
                        .build());//调用构造器对象的build方法构建SetBucketPolicyArgs对象

            }
            //以下是有该桶或者创建好桶后执行的操作

            //配置文件存储在Minio中的文件名
            String filename = new SimpleDateFormat("yyyyMMdd").format(new Date()) +
                    "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

            //上传文件
            //调用构造器对象的putObject方法上传文件
            minioClient.putObject(PutObjectArgs.builder()//创建构造器
                    .bucket(properties.getBucketName())//放入桶名，配置文件上传到哪个桶
                    //使用构造器的stream方法用stream刘上传
                    //参数解释：参数1：inputStream ：文件输入流 使用MultipartFile 的getInputStream获取
                    //参数解释：参数2：size ：文件大小 使用MultipartFile 的getSize获取
                    //参数解释：参数3：partSize ：分块大小 这里设置为-1 表示自动分块
                    .stream(file.getInputStream(),file.getSize(),-1)
                    .object(filename)//配置文件在桶中的名字
                    //设置contentType,这里默认是Stream类型，contentType访问的时候，浏览器会自动下载，
                    //file.getContentType()获取文件的设置contentType，传入contentType方法，
                    // 设置contentType为上传文件的 contentType，如图片会自动显示，视频会自动播放
                    .contentType(file.getContentType())
                    .build());//调用构造器对象的build方法构建PutObjectArgs对象

            //在Minio中，会为存入的对象分配一个ID(也就是URL),我们可以直接通过url访问
            //Minio中的url，由两部分组成   对象存储服务(MinIo)的端点+对象的绝对路径（桶名/文件名）
            //端点

            //这个写法等于下面注释的写法，参数1是分隔符，会将后面的参数用分隔符隔开
            String url = String.join("/",properties.getEndpoint(),properties.getBucketName(),filename);
            //String url = properties.getEndpoint() + "/" + properties.getBucketName() + "/" + filename;


            return url;




    }

    //桶权限字符串
    private String createBucketPolicyConfig(String bucketName) {

        return """
            {
              "Statement" : [ {
                "Action" : "s3:GetObject",
                "Effect" : "Allow",
                "Principal" : "*",
                "Resource" : "arn:aws:s3:::%s/*"
              } ],
              "Version" : "2012-10-17"
            }
            """.formatted(bucketName);
    }
}
