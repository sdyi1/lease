package com.nanhang.lease.web.admin.controller.apartment;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nanhang.lease.common.result.Result;
import com.nanhang.lease.model.entity.LabelInfo;
import com.nanhang.lease.model.enums.ItemType;
import com.nanhang.lease.web.admin.service.LabelInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "标签管理")
@RestController
@RequestMapping("/admin/label")
public class LabelController {

    @Autowired
    private LabelInfoService LabelInfoService;

    @Operation(summary = "（根据类型）查询标签列表")
    @GetMapping("list")
    //@RequestParam的required默认值为true，这里设置为false，表示可以传入null
  /* 这里的ItemType 传入的参数是公寓还是房间是前端传的，前端传入公寓，
     后面LambdaQueryWrapper的eq方法就会根据type来查询*/
    public Result<List<LabelInfo>> labelList(@RequestParam(required = false) ItemType type) {

        //配置查询条件
        //泛型参数 <LabelInfo> 指定的是要查询的实体类类型，而不是返回结果类型
        LambdaQueryWrapper<LabelInfo> LabelInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        /*
         * .eq()方法：等于
         * 有两个方法
         * 第一个方法：eq(字段名,值)
         * 第二个方法：eq(boolean,字段名,值)
         *
         * 方法1
         * 这是基本的相等条件方法，它会无条件地将相等条件添加到查询中。
         *
         * 方法2
         * 这个方法接受一个布尔条件作为第一个参数，只有当这个条件为true时，相等条件才会被添加到查询中，
         * 否则就是不添加条件查询
         */

       	 /*这里报错，前端传入的参数是ItemType.code 1或者 2,前端传入后端的数据是字符串类型
        * 前端传入字符串"1"，
	SpringMVC会使用@RequestParam注解(底层是WebDataBinder将HTTP的参数绑定到Conrtoller,进行转换)将参数绑定到Controller方法的参数上，(WebDataBinder带有String转Integer,Date，泛型等类型的类型转换器，这里就使用了泛型转换器)
	但是使用泛型转换器还是失败了，问题不是因为缺少name属性，
        * 而是因为：
	ItemType枚举只有有两个值：
        *	APARTMENT (code=1, name="公寓")
        * 	ROOM (code=2, name="房间")
        * 	Spring默认的枚举转换机制是根据枚举名称（APARTMENT、ROOM）而不是code值（1、2）进行转换，比如我们传入的数据是APARTMENT 或者ROOM 就可以找到，而我们传入的是1，泛型中没有这个实例，所以报错
        *
        * 解决方法：
	    * 重写泛型转换器（创建类继承接口Converter，重写泛型转换器convert）
	    * 重写转换器类所在地址
	    * web/admin/custom/converter/StringToItemTypeConverter.java
    	*
        *------------------------------------------------------------------
        * 前端传入后端的流程
        * 1前端将参数传入SpringMVC的Controller，Http向后端传入数据，传入的数据类型是字符串
        * 2SpringMVC会使用@RequestParam注解(底层是WebDataBinder将HTTP的参数绑定到Conrtoller,进行转换)将参数绑定到Controller方法的参数上，(WebDataBinder带有String转Integer,Date，泛型等类型的类型转换器，这里就使用了泛型转换器)
        * 3在绑定的过程中，会使用类型转换器将字符串"1"转换为ItemType枚举类型（这一步就是错误的原因，WebDataBinder是根据名称来转换成泛型中的实例的，比如我们传入的是APARTMENT那么会找到泛型中名为APARTMENT的实例，而这里我们传入的是1，泛型中就只有APARTMENT和ROOM连个实例，没有1，所以这里会报错）
        * （如果上面的问题我们解决了，接第四步）
        * 4然后在通过TpyeHandler将枚举类型转换为数据库中的code值(TpyeHandler是专门用于处理实体对象和数据库之间类型转换的操作的)
        * 5在这个时候，type是1，执行的Sql是：select * from label_info where type = 1
        *
        * 后端将找到的数据返回给前端
        * 1找到type=1的数据后，会将数据再次通过TpyeHandler转换成LabelInfo
        * 2然后将LabelInfo对象通过SpringMvc的HttpMessageConverter转换为Json字符串
        * 3最后将Json字符串返回给前端
        *
        *
        * 详情见图  D:\JavaAtGuiGuByEndHomeWork\EndProject\lease\img.png
        *------------------------------------------------------------------
        *
        *
        * */

        /*
        么type = ItemType.APARTMENT（对应code为1），这里的type是一个ItemType泛型的实体类，
        为什么默认和code做比较。getType是LabelInfo的方法，应该是用火获取数据库中数据的type值

        原因：在ItemType泛型中，我们使用了@EnumValue注解，标记code字段作为数据库存储的值，所以这里默认和code做比较
            @EnumValue标记时：插入数据时：MyBatis-Plus 会将枚举字段的值转换为对应的数据库字段值。
            @EnumValue标记时：查询数据时：MyBatis-Plus 会将数据库字段值转换为对应的枚举实例。
         */

        /*
        LambdaQueryWrapper.eq：
           type!=null： 当结果为true的时候，会再Sql语句中添加调价 where... ...佛则就是不带条件的查询
           LabelInfo::getType：
                指向 LabelInfo 类的 getType() 方法
                MyBatis-Plus 会通过这个方法引用推断出对应的数据库字段名（通常是 type）
                表示要对 LabelInfo 表的 type 字段进行等于比较
           type:这是要比较的值，即查询条件中 type 字段要等于的值

        * */
        LabelInfoLambdaQueryWrapper.eq(type!=null,LabelInfo::getType,type);
        List<LabelInfo> list = LabelInfoService.list(LabelInfoLambdaQueryWrapper);
        return Result.ok(list);
    }

    @Operation(summary = "新增或修改标签信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdateLabel(@RequestBody LabelInfo labelInfo) {
        LabelInfoService.saveOrUpdate(labelInfo);
        return Result.ok();
    }

    @Operation(summary = "根据id删除标签信息")
    @DeleteMapping("deleteById")
    public Result deleteLabelById(@RequestParam Long id) {
        LabelInfoService.removeById(id);
        return Result.ok();
    }
}
