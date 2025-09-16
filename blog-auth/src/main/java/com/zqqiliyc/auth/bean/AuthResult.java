package com.zqqiliyc.auth.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-07-01
 */
@Getter
@Setter
public class AuthResult {
    /**
     * 访问令牌
     */
    private String accessToken;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 有效时间(访问令牌),单位秒
     */
    private int expiresIn;

    public AuthResult() {
    }

    public AuthResult(String accessToken, String refreshToken, int expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }
}
