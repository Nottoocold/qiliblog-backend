package com.zqqiliyc.framework.web.http;

import com.zqqiliyc.framework.web.exception.ClientException;
import com.zqqiliyc.framework.web.spring.SpringEnvUtils;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
        // 提取前3个错误详情
        String errorMsg = ex.getBindingResult().getFieldErrors().stream()
                .limit(3)
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(","));

        return ApiResult.error(HttpStatus.BAD_REQUEST.value(), errorMsg);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResult<?> handleValidationException(ValidationException exception) {
        return ApiResult.error(HttpStatus.BAD_REQUEST.value(), isDev() ? exception.getMessage() : "参数错误");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<?> handleException(Exception ex) {
        log.error("[GlobalErrorHandler] handleException", ex);
        return ApiResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), isDev() ? ex.getMessage() : "服务器异常");
    }

    private boolean isDev() {
        return SpringEnvUtils.isDev();
    }
}
