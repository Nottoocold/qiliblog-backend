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
    /** 访问令牌 */
    private String accessToken;
    /** 有效时间,单位秒 */
    private long expiresIn;

    public AuthResult() {
    }

    public AuthResult(String accessToken, long expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }
}
