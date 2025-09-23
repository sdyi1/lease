package com.nanhang.lease.web.admin.controller.user;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nanhang.lease.common.result.Result;
import com.nanhang.lease.model.entity.UserInfo;
import com.nanhang.lease.model.enums.BaseStatus;
import com.nanhang.lease.web.admin.service.UserInfoService;
import com.nanhang.lease.web.admin.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户信息管理")
@RestController
@RequestMapping("/admin/user")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @Operation(summary = "分页查询用户信息")
    @GetMapping("page")
    public Result<IPage<UserInfo>> pageUserInfo(@RequestParam long current, @RequestParam long size, UserInfoQueryVo queryVo) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(queryVo.getPhone()!=null,UserInfo::getPhone,queryVo.getPhone());
        queryWrapper.eq(queryVo.getStatus()!=null,UserInfo::getStatus,queryVo.getStatus());
        Page<UserInfo> userInfoPage = new Page<>(current, size);
        Page<UserInfo> page = userInfoService.page(userInfoPage, queryWrapper);

        return Result.ok(page);
    }


    @Operation(summary = "根据用户id更新账号状态")
    @PostMapping("updateStatusById")
    public Result updateStatusById(@RequestParam Long id, @RequestParam BaseStatus status) {
        LambdaUpdateWrapper<UserInfo> userInfoLambdaUpdateWrapper= new LambdaUpdateWrapper<>();
        userInfoLambdaUpdateWrapper.eq(UserInfo::getId,id);
        userInfoLambdaUpdateWrapper.set(UserInfo::getStatus,status);
        return Result.ok();
    }
}
