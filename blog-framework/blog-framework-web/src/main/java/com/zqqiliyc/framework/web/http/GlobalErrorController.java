package com.zqqiliyc.framework.web.http;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.zqqiliyc.framework.web.exception.ClientException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author qili
 * @date 2025-06-28
 */
@Slf4j
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
@RequiredArgsConstructor
public class GlobalErrorController implements ErrorController {
    private final ApiErrorRenderer apiErrorRenderer;

    @ResponseBody
    @SuppressWarnings("unchecked")
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<?> handleApiError(HttpServletRequest request, HttpServletResponse response) {
        Throwable ex = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        Integer status = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String requestUri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        HttpStatus httpStatus = HttpStatus.resolve(status);

        // 服务端日志一律记录完整堆栈，对外文案的环境差异由 ApiErrorRenderer 决定
        log.error("错误请求 uri: {}, status: {}", requestUri, status, ex);

        if (ExceptionUtil.isCausedBy(ex, AuthenticationException.class)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            AuthenticationException exception = (AuthenticationException) ExceptionUtil.getCausedBy(ex, AuthenticationException.class);
            return apiErrorRenderer.render(HttpStatus.UNAUTHORIZED.value(), exception, "认证失败");
        } else if (ExceptionUtil.isCausedBy(ex, AccessDeniedException.class)) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            AccessDeniedException exception = (AccessDeniedException) ExceptionUtil.getCausedBy(ex, AccessDeniedException.class);
            return apiErrorRenderer.render(HttpStatus.FORBIDDEN.value(), exception, "无权限访问");
        } else if (ExceptionUtil.isCausedBy(ex, ClientException.class)) {
            response.setStatus(HttpStatus.OK.value());
            ClientException exception = (ClientException) ExceptionUtil.getCausedBy(ex, ClientException.class);
            return ApiResult.error(exception.getStatus(), exception.getMessage());
        }

        if (null != httpStatus) {
            return apiErrorRenderer.render(status, ex, httpStatus.getReasonPhrase());
        }
        return ApiResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }
}
