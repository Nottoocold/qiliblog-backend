package com.zqqiliyc.common.web.http;

import com.zqqiliyc.common.exception.ClientException;
import com.zqqiliyc.common.spring.SpringEnvUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.stream.Collectors;

/**
 * @author qili
 * @date 2025-06-28
 */
@Slf4j
@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ApiResult<?> handleException(AuthenticationException e) {
        return ApiResult.error(HttpStatus.UNAUTHORIZED.value(), isDev() ? e.getMessage() : "认证失败");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ApiResult<?> handleException(AccessDeniedException e) {
        return ApiResult.error(HttpStatus.FORBIDDEN.value(), isDev() ? e.getMessage() : "无权限访问");
    }

    @ExceptionHandler(ClientException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ApiResult<?> handleException(ClientException e) {
        return ApiResult.error(e.getStatus(), e.getMessage());
    }

    // 处理数据校验异常（如 @Valid 失败）
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> handleValidationException(BindException ex) {
        // 提取校验错误详情
        String errorMsg = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(","));
        return ApiResult.error(HttpStatus.BAD_REQUEST.value(), errorMsg);
    }

    private boolean isDev() {
        return SpringEnvUtils.isDev();
    }
}
