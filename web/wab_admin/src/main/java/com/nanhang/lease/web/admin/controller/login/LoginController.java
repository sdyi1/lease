package com.nanhang.lease.web.admin.controller.login;


import com.nanhang.lease.common.result.Result;
import com.nanhang.lease.web.admin.service.LoginService;
import com.nanhang.lease.web.admin.vo.login.CaptchaVo;
import com.nanhang.lease.web.admin.vo.login.LoginVo;
import com.nanhang.lease.web.admin.vo.system.user.SystemUserInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "后台管理系统登录管理")
@RestController
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Operation(summary = "获取图形验证码")
    @GetMapping("login/captcha")
    public Result<CaptchaVo> getCaptcha() {
       CaptchaVo result =loginService.getCaptche();
        return Result.ok(result);
    }

    @Operation(summary = "登录")
    @PostMapping("login")
    public Result<String> login(@RequestBody LoginVo loginVo) {
        String tooken = loginService.login(loginVo);
        return Result.ok(tooken);
    }

    @Operation(summary = "获取登陆用户个人信息")
    @GetMapping("info")
    public Result<SystemUserInfoVo> info() {
        return Result.ok();
    }
}