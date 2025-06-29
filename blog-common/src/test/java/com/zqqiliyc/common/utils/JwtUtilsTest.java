package com.zqqiliyc.common.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.zqqiliyc.common.config.config.JwtProperties;
import org.junit.jupiter.api.*;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author qili
 * @date 2025-06-22
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JwtUtilsTest {
    static JwtUtils jwtUtils;

    @BeforeAll
    public static void beforeAll() {
        JwtProperties jwtProperties = new JwtProperties();
        String secret = Base64.encode(KeyUtil.generateKey(HmacAlgorithm.HmacSHA256.getValue(), 256).getEncoded());
        jwtProperties.setSecret(secret);
        jwtUtils = new JwtUtils(jwtProperties);
    }

    @Order(0)
    @Test
    public void test0() {
        String token = jwtUtils.create("qili", 60 * 5);
        assertNotNull(token);

        assertEquals("qili", jwtUtils.getSubject(token));

        assertTrue(jwtUtils.verify(token));
    }

    @Order(1)
    @Test
    public void test1() throws InterruptedException {
        String token = jwtUtils.create("qili", 5);
        assertNotNull(token);

        assertEquals("qili", jwtUtils.getSubject(token));

        TimeUnit.MILLISECONDS.sleep(5500);
        assertFalse(jwtUtils.verify(token));
    }
}