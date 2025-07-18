package com.zqqiliyc.auth.config.shiro;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.auth.manager.AuthManager;
import com.zqqiliyc.common.token.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Role;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author qili
 * @date 2025-06-28
 */
@Slf4j
@Component
public class JwtTokenRealm extends AuthorizingRealm {
    @Autowired @Lazy
    private TokenProvider tokenProvider;
    @Autowired @Lazy
    private AuthManager authManager;

    @Override
    public boolean supports(AuthenticationToken token) {
        log.info("check supports token: {}", token.getClass().getName());
        return BearerToken.class.isAssignableFrom(token.getClass());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String bearerToken = (String) principals.getPrimaryPrincipal();
        Map<String, Object> claims = tokenProvider.getClaims(bearerToken);
        long userId = Convert.toLong(claims.get("sub"));
        Set<String> roles = Arrays.stream(StrUtil.splitToArray(String.valueOf(claims.get("roles")), ","))
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toUnmodifiableSet());
        Set<String> permissions = authManager.getPermissions(userId);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roles);
        authorizationInfo.setStringPermissions(permissions);
        if (log.isDebugEnabled()) {
            log.debug("get authorization info, roles: {}", roles);
            log.debug("get authorization info, permissions size: {}", permissions.size());
        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        BearerToken bearerToken = (BearerToken) token;
        if (bearerToken.getToken() == null) {
            throw new AuthenticationException("token is null");
        }
        if (!tokenProvider.validateToken(bearerToken.getToken())) {
            throw new AuthenticationException("token is invalid");
        }
        if (log.isDebugEnabled()) {
            log.debug("get authentication info, jwt token valid.");
        }
        // jwt token 没有密码
        return new SimpleAuthenticationInfo(bearerToken.getPrincipal(), bearerToken.getCredentials(), getName());
    }
}
