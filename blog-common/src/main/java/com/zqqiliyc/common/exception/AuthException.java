package com.zqqiliyc.common.exception;

import com.zqqiliyc.common.enums.AuthState;
import lombok.Getter;

/**
 * 登录认证异常
 * @author qili
 * @date 2025-07-01
 */
@Getter
public class AuthException extends RuntimeException {
    private final int status;

    public AuthException(AuthState authState) {
        super(authState.getMessage());
        this.status = authState.getCode();
    }
}
