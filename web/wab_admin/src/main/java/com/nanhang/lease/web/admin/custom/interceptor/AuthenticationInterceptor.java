package com.nanhang.lease.web.admin.custom.interceptor;

import com.nanhang.lease.common.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

//将AuthenticationInterceptor类注册为Spring组件，用后续直接注入
@Component
//实现HandlerInterceptor接口，用于拦截请求，判断是否有token，有则放行，无则返回未登录错误
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //一般token是放在请求头中的，所以这里需要从请求头中获取token
        //获取请求头，请求头是键值对形式，更具我们这里传入的键去找对应的值，具体键叫什么名字需要和前端约定好
        String token = request.getHeader("access-token");
//        将token传入验证的方法中验证是否过期或者非法或者为null
        JwtUtil.parseToken(token);
        //如果没有异常，说明token验证通过，放行
        return true;
    }
}
