package com.nanhang.lease.common.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    private static SecretKey tokenSignKey = Keys.hmacShaKeyFor("M0PKKI6pYGVWWfDZw90a0lTpGYX1d4AQ".getBytes());

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
}
