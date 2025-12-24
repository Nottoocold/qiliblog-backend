package com.zqqiliyc.module.auth.provider;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.framework.web.bean.AuthUserInfoBean;
import com.zqqiliyc.framework.web.constant.SystemConstants;
import com.zqqiliyc.framework.web.token.TokenProvider;
import com.zqqiliyc.module.auth.AuthManager;
import com.zqqiliyc.module.auth.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author qili
 * @date 2025-12-23
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final AuthManager authManager;
    private final TokenProvider tokenProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String accessToken = Convert.toStr(jwtAuthenticationToken.getPrincipal());
        if (StrUtil.isNotBlank(accessToken) && tokenProvider.verifyAccessToken(accessToken)) {
            // token有效，设置用户信息
            Map<String, Object> claims = tokenProvider.getClaims(accessToken);
            Long userId = Convert.toLong(claims.get(SystemConstants.CLAIM_SUBJECT));
            AuthUserInfoBean userDetails = authManager.getUserInfo(userId);
            return JwtAuthenticationToken.authenticated(userDetails);
        }

        throw new BadCredentialsException("invalid token");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
