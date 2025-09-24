package com.nanhang.lease.web.admin.controller.system;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nanhang.lease.common.result.Result;
import com.nanhang.lease.model.entity.SystemUser;
import com.nanhang.lease.model.enums.BaseStatus;
import com.nanhang.lease.web.admin.service.SystemUserService;
import com.nanhang.lease.web.admin.vo.system.user.SystemUserItemVo;
import com.nanhang.lease.web.admin.vo.system.user.SystemUserQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Tag(name = "后台用户信息管理")
@RestController
@RequestMapping("/admin/system/user")
public class SystemUserController {
    @Autowired
    private SystemUserService systemUserService;

    @Operation(summary = "根据条件分页查询后台用户列表")
    @GetMapping("page")
    public Result<IPage<SystemUserItemVo>> page(@RequestParam long current, @RequestParam long size, SystemUserQueryVo queryVo) {

        Page<SystemUserItemVo> page = new Page<>(current, size);
        IPage<SystemUserItemVo> Ipage = systemUserService.selectByPage(page,queryVo);

        return Result.ok(Ipage);
    }

    @Operation(summary = "根据ID查询后台用户信息")
    @GetMapping("getById")
    public Result<SystemUserItemVo> getById(@RequestParam Long id) {
        SystemUserItemVo systemUserItemVo = systemUserService.getByIdDiy(id);
        return Result.ok(systemUserItemVo);
    }

    @Operation(summary = "保存或更新后台用户信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody SystemUser systemUser) {
        systemUserService.saveOrUpdate(systemUser);
        return Result.ok();
    }

    @Operation(summary = "判断后台用户名是否可用")
    @GetMapping("isUserNameAvailable")
    public Result<Boolean> isUsernameExists(@RequestParam String username) {
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemUser::getUsername,username);
        SystemUser systemUser = systemUserService.getBaseMapper().selectOne(queryWrapper);
        boolean b = systemUser.getStatus() == BaseStatus.ENABLE;
        return Result.ok(b);
    }

    @DeleteMapping("deleteById")
    @Operation(summary = "根据ID删除后台用户信息")
    public Result removeById(@RequestParam Long id) {
        systemUserService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "根据ID修改后台用户状态")
    @PostMapping("updateStatusByUserId")
    public Result updateStatusByUserId(@RequestParam Long id, @RequestParam BaseStatus status) {
        LambdaUpdateWrapper<SystemUser> systemUserLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        systemUserLambdaUpdateWrapper.eq(SystemUser::getId,id);
        systemUserLambdaUpdateWrapper.set(SystemUser::getStatus,status);
        systemUserService.update(null,systemUserLambdaUpdateWrapper);
        return Result.ok();
    }
}
