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

        /*

        总结：问题多多，不可取

        //自己初步尝试
            //每次添加都要设置为未删除(问题当修改成1的时候也会执行这段代码导致修改失败，但不报错)
        paymentType.setIsDeleted((byte)0);

            //获取当前时间，赋值给修改时间
        paymentType.setUpdateTime(new Date());

        */


        //设置默认is_deleted为0,
        //  为了解决上面的问题我们选择再数据库中设置该列默认值为0
        //这样，当我们需要修改为1的时候就不会重新set成0了


        //将对象存入
        paymentTypeService.saveOrUpdate(paymentType);
        return Result.ok();
    }

    @Operation(summary = "根据ID删除支付方式")
    @DeleteMapping("deleteById")
//    @RequestParam 是 Spring MVC 中的一个注解，用于将 HTTP 请求中的参数绑定到方法的参数上
    public Result deletePaymentById(@RequestParam Long id) {
       /*
       执行的是逻辑删除，我们在实体类中的isDeleted属性上面设置了
        @TableField("is_deleted") + @TableLogic
        @TableLogic配置了逻辑删除，当没有配置这个注解的时候，removeById执行的就是物理删除（真的删除掉了数据）


        实际执行的SQL语句是：UPDATE payment_type SET update_time=?, is_deleted=1 WHERE id=? AND is_deleted=0*/
        paymentTypeService.removeById(id);
        return Result.ok();


    }

}















