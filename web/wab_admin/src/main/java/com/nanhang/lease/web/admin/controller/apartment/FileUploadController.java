package com.nanhang.lease.web.admin.controller.apartment;


import com.nanhang.lease.common.result.Result;
import com.nanhang.lease.web.admin.service.FileService;
import io.minio.errors.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


@Tag(name = "文件管理")
@RequestMapping("/admin/file")
@RestController
public class FileUploadController {

    @Autowired
    private FileService fileService;

    @Operation(summary = "上传文件")
    @PostMapping("upload")
    //MultipartFile是Spring框架提供的专门用于上传文件的类型
    /*
    上传图片的步骤
        1首先我们添加房间的时候，填好名称，地址后，点击添加图片后，前端会访问上传图片的接口
        2后端接收到图片后，会将图片保存在MinIo当中
        3将我们图片在minio中的url返回给前端
        4前端会调用后端添加房间的接口，将图片的url和其他信息一起提交给数据库
     */
    public Result<String> upload(@RequestParam MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        /*
         * MultipartFile的方法解析
         * getSize()获取文件大小
         * getBytes():将文件放入字节数组，一次性上传（适合上传小文件）
         * getInputStream() 分多次上传文件，一次上传一点（适合上传大文件）
         * getName():获取文件在url中的名称
         * getOriginalFilename()：获取原文件名
         *
         * */
        String url = fileService.upload(file);

        return Result.ok(url);
    }




}
