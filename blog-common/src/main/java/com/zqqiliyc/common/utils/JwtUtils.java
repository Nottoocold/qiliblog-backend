package com.zqqiliyc.common.utils;

import cn.hutool.jwt.JWT;

import java.util.Date;
import java.util.Map;

/**
 * @author qili
 * @date 2025-06-22
 */
public class JwtUtils {
    private static final byte[] KEY = "qili9iliq".getBytes();

    /**
     * 创建token
     * @param subject token主体
     * @param expireIn 过期时间 单位秒
     * @return token
     */
    public static String create(String subject, long expireIn) {
        JWT jwt = JWT.create();
        long cur = System.currentTimeMillis(); // 当前时间戳
        Date date = new Date(cur); // 当前时间
        Date exp = new Date(cur + expireIn * 1000);
        jwt.setIssuedAt(date); // 签发时间
        jwt.setExpiresAt(exp); // 过期时间
        jwt.addPayloads(Map.of(JWT.SUBJECT, subject));
        jwt.setKey(KEY);
        return jwt.sign();
    }

    public static boolean verify(String token) {
        return JWT.of(token).setKey(KEY).validate(0);
    }

    public static String getSubject(String token) {
        return (String) JWT.of(token).setKey(KEY).getPayload().getClaim(JWT.SUBJECT);
    }
}
