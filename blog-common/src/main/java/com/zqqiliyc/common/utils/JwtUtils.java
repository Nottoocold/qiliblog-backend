package com.zqqiliyc.common.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.zqqiliyc.common.config.config.JwtProperties;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * @author qili
 * @date 2025-06-22
 */
public class JwtUtils {
    private final JwtProperties jwtProperties;

    public JwtUtils(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    /**
     * 创建token
     * @param subject token主体
     * @return token
     */
    public String create(String subject) {
        return create(subject, jwtProperties.getExpire());
    }

    /**
     * 创建token
     * @param subject token主体
     * @param expireIn 过期时间 单位秒
     * @return token
     */
    public String create(String subject, long expireIn) {
        return create(subject, Collections.emptyMap(), expireIn);
    }

    /**
     * 创建token
     * @param subject token主体
     * @param claims 载荷
     * @param expireIn 过期时间 单位秒
     * @return token
     */
    public String create(String subject, Map<String, Object> claims, long expireIn) {
        JWT jwt = JWT.create();
        jwt.setIssuedAt(new Date()); // 签发时间
        jwt.setExpiresAt(new Date(System.currentTimeMillis() + expireIn * 1000)); // 过期时间
        jwt.setSubject(subject); // 主题
        jwt.addPayloads(claims);
        jwt.setSigner(JWTSignerUtil.hs256(getKey(jwtProperties)));
        return jwt.sign();
    }

    public boolean verify(String token) {
        return JWT.of(token).setSigner(JWTSignerUtil.hs256(getKey(jwtProperties))).validate(0);
    }

    public String getSubject(String token) {
        return (String) JWT.of(token)
                .setSigner(JWTSignerUtil.hs256(getKey(jwtProperties)))
                .getPayload()
                .getClaim(JWT.SUBJECT);
    }

    public Map<String, Object> getClaims(String token) {
        return JWT.of(token)
                .setSigner(JWTSignerUtil.hs256(getKey(jwtProperties)))
                .getPayloads();
    }

    private byte[] getKey(JwtProperties jwtProperties) {
        return Base64.decode(jwtProperties.getSecret());
    }
}
