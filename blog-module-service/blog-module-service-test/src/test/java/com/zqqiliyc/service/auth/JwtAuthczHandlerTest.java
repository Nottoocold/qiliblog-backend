package com.zqqiliyc.service.auth;

import com.zqqiliyc.BaseTestApp;
import com.zqqiliyc.framework.web.json.JsonHelper;
import com.zqqiliyc.module.auth.config.JwtAuthczHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Map;

/**
 * MVC 之外的认证/授权失败出口：JSON 响应文案同样由 ApiErrorRenderer 决定，
 * test 环境（本模块 profile）只输出中文兜底文案，不泄漏异常细节
 */
public class JwtAuthczHandlerTest extends BaseTestApp {
    private static final String SECRET_DETAIL = "token 解析失败, secret=QHB9T328CqtUnRM";
    private static final String PROTECTED_URL = "/api/admin/post/1";

    @Autowired
    JwtAuthczHandler jwtAuthczHandler;
    @Autowired
    Environment environment;

    @Test
    @Order(0)
    public void testCommenceHidesDetailInTestProfile() throws Exception {
        Assertions.assertTrue(environment.acceptsProfiles(Profiles.of("test")),
                "前提：本模块运行在 test profile");
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtAuthczHandler.commence(new MockHttpServletRequest("GET", PROTECTED_URL), response,
                new BadCredentialsException(SECRET_DETAIL));

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        Assertions.assertEquals("application/json;charset=utf-8", response.getContentType());
        Map<?, ?> body = JsonHelper.fromJson(response.getContentAsString(), Map.class);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), ((Number) body.get("errorCode")).intValue());
        Assertions.assertEquals("认证失败", body.get("errorDesc"), "test 环境不得输出原始异常消息");
    }

    @Test
    @Order(1)
    public void testHandleHidesDetailInTestProfile() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtAuthczHandler.handle(new MockHttpServletRequest("GET", PROTECTED_URL), response,
                new AccessDeniedException(SECRET_DETAIL));

        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
        Assertions.assertEquals("application/json;charset=utf-8", response.getContentType());
        Map<?, ?> body = JsonHelper.fromJson(response.getContentAsString(), Map.class);
        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), ((Number) body.get("errorCode")).intValue());
        Assertions.assertEquals("无权限访问", body.get("errorDesc"), "test 环境不得输出原始异常消息");
    }
}