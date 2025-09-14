package com.nanhang.lease.common.exception;

import com.nanhang.lease.common.result.ResultCodeEnum;
import lombok.Data;

@Data
public class LeaseException extends RuntimeException {
    private Integer code;
    // 自定义异常
    public LeaseException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    //通过直接传入resultCodeEnum对象传递 code 和 message
    public LeaseException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();


    }

}
