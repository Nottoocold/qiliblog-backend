package com.zqqiliyc.auth;

import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-07-01
 */
@Getter
@Setter
public class AuthResult {

    private int status;

    private boolean error;

    private String message;

    private String accessToken;

    private long expiresIn;

    public AuthResult() {
    }

    public AuthResult(int status, boolean error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }
}
