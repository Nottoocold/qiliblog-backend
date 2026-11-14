package com.zqqiliyc.module.auth.config;

import com.zqqiliyc.framework.web.http.ApiErrorRenderer;
import com.zqqiliyc.framework.web.json.JsonHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 认证和授权失败处理器<br>
 * 因为是纯API服务，直接返回错误信息即可
 *
 * @author zqqiliyc
 * @since 2025-07-20
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthczHandler implements AuthenticationEntryPoint, AccessDeniedHandler {
    private final ApiErrorRenderer apiErrorRenderer;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (log.isDebugEnabled()) {
            log.warn("Authentication failed: {}", authException.getMessage());
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=utf-8");
        JsonHelper.toJson(apiErrorRenderer.render(HttpStatus.UNAUTHORIZED.value(), authException, "认证失败"), response.getOutputStream());
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if (log.isDebugEnabled()) {
            log.warn("Access denied: {}", accessDeniedException.getMessage());
        }
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json;charset=utf-8");
        JsonHelper.toJson(apiErrorRenderer.render(HttpStatus.FORBIDDEN.value(), accessDeniedException, "无权限访问"), response.getOutputStream());
    }
}
