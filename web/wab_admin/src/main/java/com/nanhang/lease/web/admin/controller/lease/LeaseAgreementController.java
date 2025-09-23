package com.nanhang.lease.web.admin.controller.lease;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nanhang.lease.common.result.Result;
import com.nanhang.lease.model.entity.LeaseAgreement;
import com.nanhang.lease.model.enums.LeaseStatus;
import com.nanhang.lease.web.admin.service.LeaseAgreementService;
import com.nanhang.lease.web.admin.vo.agreement.AgreementQueryVo;
import com.nanhang.lease.web.admin.vo.agreement.AgreementVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Tag(name = "租约管理")
@RestController
@RequestMapping("/admin/agreement")
public class    LeaseAgreementController {

    @Autowired
    private LeaseAgreementService leaseAgreementService;

    @Operation(summary = "保存或修改租约信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody LeaseAgreement leaseAgreement) {
        leaseAgreementService.saveOrUpdate(leaseAgreement);
        return Result.ok();
    }

    @Operation(summary = "根据条件分页查询租约列表")
    @GetMapping("page")
    public Result<IPage<AgreementVo>> page(@RequestParam long current, @RequestParam long size, AgreementQueryVo queryVo) {

        Page page = new Page(current, size);
        IPage<AgreementVo> agreementVoIPage = leaseAgreementService.selectByPage(page, queryVo);
        return Result.ok(agreementVoIPage);
    }

    @Operation(summary = "根据id查询租约信息")
    @GetMapping(name = "getById")
    public Result<AgreementVo> getById(@RequestParam Long id) {
        AgreementVo agreementVo = leaseAgreementService.selectByIdDyi(id);
        return Result.ok(agreementVo);
    }

    @Operation(summary = "根据id删除租约信息")
    @DeleteMapping("removeById")
    public Result removeById(@RequestParam Long id) {
        leaseAgreementService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "根据id更新租约状态")
    @PostMapping("updateStatusById")
    public Result updateStatusById(@RequestParam Long id, @RequestParam LeaseStatus status) {
        leaseAgreementService.updateStatusById(id, status);
        return Result.ok();
    }

}



