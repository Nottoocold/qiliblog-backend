package com.zqqiliyc.module.auth.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author qili
 * @date 2025-12-23
 */
public class PasswordAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;
    private final Object credentials;

    private PasswordAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    private PasswordAuthenticationToken(Object principal, Object credentials,
                                        Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(true);
    }

    public static PasswordAuthenticationToken unauthenticated(String username, String password) {
        return new PasswordAuthenticationToken(username, password);
    }

    public static PasswordAuthenticationToken authenticated(UserDetails userDetails) {
        return new PasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
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
