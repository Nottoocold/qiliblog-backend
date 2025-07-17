package com.zqqiliyc.common.web.http;

import cn.hutool.core.util.ArrayUtil;
import com.zqqiliyc.common.exception.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
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
public class GlobalErrorHandler implements EnvironmentAware {
    private String[] profiles;

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ApiResult<?> handleException(AuthenticationException e) {
        return ApiResult.error(HttpStatus.UNAUTHORIZED.value(), isDev() ? e.getMessage() : "认证失败");
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ApiResult<?> handleException(AuthorizationException e) {
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
        return ArrayUtil.contains(profiles, "dev");
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.profiles = environment.getActiveProfiles();
    }
}
