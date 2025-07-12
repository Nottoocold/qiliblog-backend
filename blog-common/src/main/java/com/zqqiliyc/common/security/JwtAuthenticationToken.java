package com.zqqiliyc.common.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * @author qili
 * @date 2025-07-12
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final String token;

    public JwtAuthenticationToken(String token) {
        super(null);
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
