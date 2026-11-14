package com.zqqiliyc.module.auth.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author qili
 * @date 2025-12-23
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;
    private final Object credentials;

    private JwtAuthenticationToken(Object principal, Object credentials) {
        super((Collection<? extends GrantedAuthority>) null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    private JwtAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(true);
    }

    public static JwtAuthenticationToken unauthenticated(String jwtToken) {
        return new JwtAuthenticationToken(jwtToken, null);
    }

    /**
     * 认证成功的结果：principal 是用户信息，credentials 保留原始令牌串，
     * 撤销令牌等后续操作从认证结果取令牌，不再重复解析请求
     *
     * @param userDetails 用户信息
     * @param accessToken 已校验的访问令牌
     * @return 已认证的令牌
     */
    public static JwtAuthenticationToken authenticated(UserDetails userDetails, String accessToken) {
        return new JwtAuthenticationToken(userDetails, accessToken, userDetails.getAuthorities());
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
