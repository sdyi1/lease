package com.nanhang.lease.web.admin.controller.lease;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nanhang.lease.common.result.Result;
import com.nanhang.lease.model.entity.ViewAppointment;
import com.nanhang.lease.model.enums.AppointmentStatus;
import com.nanhang.lease.web.admin.service.ViewAppointmentService;
import com.nanhang.lease.web.admin.vo.appointment.AppointmentQueryVo;
import com.nanhang.lease.web.admin.vo.appointment.AppointmentVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Tag(name = "预约看房管理")
@RequestMapping("/admin/appointment")
@RestController
public class ViewAppointmentController {

    @Autowired
    private ViewAppointmentService viewAppointmentService;

    @Operation(summary = "分页查询预约信息")
    @GetMapping("page")
    public Result<IPage<AppointmentVo>> page(@RequestParam long current, @RequestParam long size, AppointmentQueryVo queryVo) {
        Page<AppointmentVo> appointmentVoPage = new Page<>(current, size);
        IPage<AppointmentVo> appointmentVoIpage = viewAppointmentService.selectIPage(appointmentVoPage, queryVo);
        return Result.ok(appointmentVoIpage);
    }

    @Operation(summary = "根据id更新预约状态")
    @PostMapping("updateStatusById")
    public Result updateStatusById(@RequestParam Long id, @RequestParam AppointmentStatus status) {
        LambdaUpdateWrapper<ViewAppointment> ViewAppointmentUpdateWrapper = new LambdaUpdateWrapper<>();
        ViewAppointmentUpdateWrapper.eq(ViewAppointment::getId,id);
        ViewAppointmentUpdateWrapper.set(ViewAppointment::getAppointmentStatus,status);
        viewAppointmentService.update(ViewAppointmentUpdateWrapper);
        return Result.ok();
    }

}
