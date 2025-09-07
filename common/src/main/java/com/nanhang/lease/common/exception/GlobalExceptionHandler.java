package com.nanhang.lease.common.exception;

import com.nanhang.lease.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
//异常全局处理器
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)//参数传入你要处理的异常类型
    @ResponseBody
    public Result error(Exception e){

        e.printStackTrace();
        return Result.fail();


    }
}
