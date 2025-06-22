package com.zqqiliyc.common.jwt;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qili
 * @date 2025-06-22
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JwtTest {
    static final byte[] KEY = "qili9iliq".getBytes();

    @Order(0)
    @Test
    public void testCreateToken() {
        String token = getToken();
        Assertions.assertNotNull(token);
    }

    @Order(1)
    @Test
    public void testParseToken() {
        String token = getToken();
        JWT jwt = JWTUtil.parseToken(token);
        System.out.println("jwt header: " + jwt.getHeader());
        System.out.println("jwt payload: " + jwt.getPayload());
    }

    @Order(2)
    @Test
    public void testVerifyToken() {
        String token = getToken();
        boolean verify = JWTUtil.verify(token, KEY);
        boolean validate = JWT.of(token).setKey(KEY).validate(0);
        Assertions.assertTrue(verify);
        Assertions.assertTrue(validate);
    }

    private String getToken() {
        JWT jwt = JWT.create();
        long cur = System.currentTimeMillis(); // 当前时间戳
        Date date = new Date(cur); // 当前时间
        Date exp = new Date(cur + 3600 * 1000); // 3600秒后过期
        jwt.setIssuedAt(date); // 签发时间
        jwt.setExpiresAt(exp); // 过期时间
        jwt.addPayloads(getPayload());
        jwt.setKey(KEY);
        return jwt.sign();
    }

    private Map<String, Object> getPayload() {
        Map<String, Object> payload = new HashMap<>();
        /*LocalDateTime now = LocalDateTime.now(); // 当前时间
        LocalDateTime exp = LocalDateTimeUtil.offset(now, 3600, ChronoUnit.SECONDS);// 3600秒后过期
        payload.put(JWT.ISSUED_AT, now); // jwt签发时间
        payload.put(JWT.EXPIRES_AT, exp); // jwt过期时间*/

        payload.put("name", "qili");
        payload.put("other", "info");
        return payload;
    }
}
