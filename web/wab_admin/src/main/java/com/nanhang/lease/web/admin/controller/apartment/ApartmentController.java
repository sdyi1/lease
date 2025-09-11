package com.nanhang.lease.web.admin.controller.apartment;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nanhang.lease.common.result.Result;
import com.nanhang.lease.model.entity.ApartmentInfo;
import com.nanhang.lease.model.enums.ReleaseStatus;
import com.nanhang.lease.web.admin.service.*;
import com.nanhang.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.nanhang.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.nanhang.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.nanhang.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.PrivateKey;
import java.util.List;


@Tag(name = "公寓信息管理")
@RestController
@RequestMapping("/admin/apartment")
public class ApartmentController {

    @Autowired
    private ApartmentInfoService apartmentInfoService;







    @Operation(summary = "保存或更新公寓信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody ApartmentSubmitVo apartmentSubmitVo) {
      /*使用自带的方法，会更具@TablezName的表注解来映射数据
        而这里的表没有ApartmentSubmitVo属性对应的列，所以我们不能使用自带的方法而是需要字节写方法*/
         apartmentInfoService.saveOrUpdateApartment(apartmentSubmitVo);
        return Result.ok();
    }

    @Operation(summary = "根据条件分页查询公寓列表")
    @GetMapping("pageItem")
    public Result<IPage<ApartmentItemVo>> pageItem(@RequestParam long current, @RequestParam long size, ApartmentQueryVo queryVo) {
        //将页数和单页长度传入Page
        Page<ApartmentItemVo> apartmentItemVoPage = new Page(current, size);
        IPage<ApartmentItemVo> ipageApartment =  apartmentInfoService.selectIPage(apartmentItemVoPage,queryVo);

        return Result.ok(ipageApartment);
    }

    @Operation(summary = "根据ID获取公寓详细信息")
    @GetMapping("getDetailById")
    public Result<ApartmentDetailVo> getDetailById(@RequestParam Long id) {
        
        return Result.ok();
    }

    @Operation(summary = "根据id删除公寓信息")
    @DeleteMapping("removeById")
    public Result removeById(@RequestParam Long id) {
        return Result.ok();
    }

    @Operation(summary = "根据id修改公寓发布状态")
    @PostMapping("updateReleaseStatusById")
    public Result updateReleaseStatusById(@RequestParam Long id, @RequestParam ReleaseStatus status) {
        return Result.ok();
    }

    @Operation(summary = "根据区县id查询公寓信息列表")
    @GetMapping("listInfoByDistrictId")
    public Result<List<ApartmentInfo>> listInfoByDistrictId(@RequestParam Long id) {
        return Result.ok();
    }
}














