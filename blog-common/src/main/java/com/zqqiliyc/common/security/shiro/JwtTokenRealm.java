package com.zqqiliyc.common.security.shiro;

import com.zqqiliyc.common.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Role;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author qili
 * @date 2025-06-28
 */
@Slf4j
public class JwtTokenRealm extends AuthorizingRealm {
    private final JwtUtils jwtUtils;

    public JwtTokenRealm(JwtUtils jwtUtils) {
        super();
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        log.info("check supports token: {}", token.getClass().getName());
        return BearerToken.class.isAssignableFrom(token.getClass());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String bearerToken = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(Set.of("admin"));
        authorizationInfo.setStringPermissions(Set.of("user:read"));
        log.info("get authorization info: {}", authorizationInfo);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        BearerToken bearerToken = (BearerToken) token;
        if (bearerToken.getToken() == null) {
            throw new AuthenticationException("token is null");
        }
        if (!jwtUtils.verify(bearerToken.getToken())) {
            throw new AuthenticationException("token is invalid");
        }
        log.info("get authentication info, jwt token valid.");
        // jwt token 没有密码
        return new SimpleAuthenticationInfo(bearerToken.getPrincipal(), bearerToken.getCredentials(), getName());
    }
}
