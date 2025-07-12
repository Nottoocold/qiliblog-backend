package com.zqqiliyc.common.security;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import com.zqqiliyc.common.web.http.ApiResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * @author qili
 * @date 2025-07-12
 */
@Slf4j
public class ForbiddenAccessDenyHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("ForbiddenAccessDenyHandler handle: {}", accessDeniedException.getMessage(), accessDeniedException);
        }
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json;charset=UTF-8");
        IoUtil.writeUtf8(response.getOutputStream(), false, JSONUtil.toJsonStr(ApiResult.error(403, "无权限访问")));
    }
}
