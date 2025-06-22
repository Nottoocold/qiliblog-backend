package com.zqqiliyc.common.utils;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author qili
 * @date 2025-06-22
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JwtUtilsTest {

    @Order(0)
    @Test
    public void test0() {
        String token = JwtUtils.create("qili", 60 * 5);
        assertNotNull(token);

        assertEquals("qili", JwtUtils.getSubject(token));

        assertTrue(JwtUtils.verify(token));
    }

    @Order(1)
    @Test
    public void test1() throws InterruptedException {
        String token = JwtUtils.create("qili", 5);
        assertNotNull(token);

        assertEquals("qili", JwtUtils.getSubject(token));

        TimeUnit.MILLISECONDS.sleep(5500);
        assertFalse(JwtUtils.verify(token));
    }
}