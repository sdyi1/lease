package com.nanhang.lease.common.utils;

import com.nanhang.lease.common.exception.LeaseException;
import com.nanhang.lease.common.result.ResultCodeEnum;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    private static SecretKey tokenSignKey = Keys.hmacShaKeyFor("M0PKKI6pYGVWWfDZw90a0lTpGYX1d4AQ".getBytes());

    //创建token
    public static String createToken(Long userId, String username) {
        String token = Jwts.builder().
                //设置jwt的主题
                setSubject("USER_INFO").
                //设置JWT过期时间
                setExpiration(new Date(System.currentTimeMillis() + 36000000)).
                //设置载荷（payload），可以存储用户信息等
                claim("userId", userId).
                claim("username", username).
                //配置token字符串的点三部分，使用密钥对jwt进行签名
                signWith(tokenSignKey).
                //生成jwt字符串
                compact();
        return token;
    }

    //解析token 验证
    public static void parseToken(String token) {

        //判断token是否为空,为空则抛出未登录异常
        if(token == null){
            throw new LeaseException(ResultCodeEnum.APP_LOGIN_AUTH);
        }
        try{

            Jwts.parserBuilder()
                    //放入密钥
                    .setSigningKey(tokenSignKey)
                    //得到解析器
                    .build()
                    //解析token
                    .parseClaimsJws(token);
            //捕获过期异常
        }catch ( ExpiredJwtException e){
            throw new LeaseException(ResultCodeEnum.TOKEN_EXPIRED);
            //捕获其他异常(发现其他异常都继承自JwtException，所以我们直接捕获JwtException)
        }catch (JwtException e){
            throw new LeaseException(ResultCodeEnum.TOKEN_INVALID);
        }
    }
}
