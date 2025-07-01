package com.zqqiliyc.common.web.http;

import com.zqqiliyc.common.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
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
        return ApiResult.error(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ApiResult<?> handleException(AuthorizationException e) {
        return ApiResult.error(HttpStatus.FORBIDDEN.value(), e.getMessage());
    }

    @ExceptionHandler(AuthException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ApiResult<?> handleException(AuthException e) {
        return ApiResult.error(e.getStatus(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResult<?> handleException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        log.warn("参数校验失败: {}", errors);
        return ApiResult.error(HttpStatus.BAD_REQUEST.value(), errors.toString());
    }

    // 处理数据校验异常（如 @Valid 失败）
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<?> handleValidationException(BindException ex) {
        // 提取校验错误详情
        String errorMsg = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ApiResult.error(HttpStatus.BAD_REQUEST.value(), errorMsg);
    }
}
