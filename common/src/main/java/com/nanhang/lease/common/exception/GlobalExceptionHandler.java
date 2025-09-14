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

    //虽然抛出RuntimeException异常，上面一个处理Exception异常的方法也会被调用，
    // 但是RuntimeException异常的处理方法会被优先调用，因为它更加具体
    @ExceptionHandler(RuntimeException.class)//参数传入你要处理的异常类型
    @ResponseBody
    public Result error(LeaseException e){
        e.printStackTrace();
        return Result.fail(e.getCode(),e.getMessage());

    }
}
