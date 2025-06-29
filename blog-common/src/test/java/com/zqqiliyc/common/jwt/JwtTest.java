package com.zqqiliyc.common.jwt;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSignerUtil;
import org.junit.jupiter.api.*;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qili
 * @date 2025-06-22
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JwtTest {
    static byte[] KEY;

    @BeforeAll
    public static void beforeAll() {
        SecretKey secretKey = KeyUtil.generateKey(HmacAlgorithm.HmacSHA256.getValue(), 256);
        String encoded = Base64.encode(secretKey.getEncoded());
        Assertions.assertArrayEquals(secretKey.getEncoded(), Base64.decode(encoded));
        System.out.println(StrUtil.format("secretKey alg {}, format {}, value->{}",
                secretKey.getAlgorithm(), secretKey.getFormat(), encoded));
        KEY = secretKey.getEncoded();
    }

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
        jwt.setIssuedAt(new Date()); // 签发时间
        jwt.setExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000)); // 过期时间
        jwt.addPayloads(getPayload());
        jwt.setSigner(JWTSignerUtil.hs256(KEY));
        return jwt.sign();
    }

    private Map<String, Object> getPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "qili");
        payload.put("roles", "admin,user");
        payload.put(JWT.SUBJECT, "testsub");
        return payload;
    }
}
