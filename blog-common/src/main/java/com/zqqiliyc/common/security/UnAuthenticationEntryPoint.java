package com.zqqiliyc.common.security;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import com.zqqiliyc.common.web.http.ApiResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * @author qili
 * @date 2025-07-12
 */
@Slf4j
public class UnAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("UnAuthenticationEntryPoint commence: {}", authException.getMessage(), authException);
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        IoUtil.writeUtf8(response.getOutputStream(), false, JSONUtil.toJsonStr(ApiResult.error(401, "未登录")));
    }
}
