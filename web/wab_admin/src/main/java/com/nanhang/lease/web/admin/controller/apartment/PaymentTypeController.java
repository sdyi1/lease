package com.nanhang.lease.web.admin.controller.apartment;


import com.nanhang.lease.common.result.Result;
import com.nanhang.lease.model.entity.PaymentType;
import com.nanhang.lease.web.admin.service.PaymentTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "支付方式管理")
@RequestMapping("/admin/payment")
@RestController
public class PaymentTypeController {

    @Autowired
    private PaymentTypeService paymentTypeService;
    @Operation(summary = "查询全部支付方式列表")
    @GetMapping("list")
    public Result<List<PaymentType>> listPaymentType() {
        List<PaymentType> list = paymentTypeService.list();
        return Result.ok(list);
    }


    @Operation(summary = "保存或更新支付方式")
    @PostMapping("saveOrUpdate")
      //requestBody注解:将json字符串转换为PaymentType对象，对象名字就是paymentType
    public Result saveOrUpdatePaymentType(@RequestBody PaymentType paymentType) {
       //将对象存入
        paymentTypeService.save(paymentType);
        return Result.ok();
    }

    @Operation(summary = "根据ID删除支付方式")
    @DeleteMapping("deleteById")
    public Result deletePaymentById(@RequestParam Long id) {
        return Result.ok();
    }

}















