package com.zqqiliyc.auth.config;

import com.zqqiliyc.framework.web.json.JsonHelper;
import com.zqqiliyc.framework.web.spring.SpringEnvUtils;
import com.zqqiliyc.framework.web.http.ApiResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
public class JwtAuthczHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (log.isDebugEnabled()) {
            log.warn("Authentication failed: {}", authException.getMessage());
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=utf-8");
        JsonHelper.toJson(ApiResult.error(HttpStatus.UNAUTHORIZED.value(), isDev() ? authException.getMessage() : "认证失败"), response.getOutputStream());
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if (log.isDebugEnabled()) {
            log.warn("Access denied: {}", accessDeniedException.getMessage());
        }
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json;charset=utf-8");
        JsonHelper.toJson(ApiResult.error(HttpStatus.FORBIDDEN.value(), isDev() ? accessDeniedException.getMessage() : "无权限访问"), response.getOutputStream());
    }

    private boolean isDev() {
        return SpringEnvUtils.isDev();
    }
}
