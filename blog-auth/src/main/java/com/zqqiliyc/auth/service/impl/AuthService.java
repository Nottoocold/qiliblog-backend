package com.zqqiliyc.auth.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.auth.AuthResult;
import com.zqqiliyc.auth.dto.LoginDto;
import com.zqqiliyc.auth.manager.AuthManager;
import com.zqqiliyc.auth.service.IAuthService;
import com.zqqiliyc.auth.strategy.AuthStrategy;
import com.zqqiliyc.auth.token.AuthRequestToken;
import com.zqqiliyc.domain.entity.SysToken;
import com.zqqiliyc.framework.web.bean.AuthUserInfoBean;
import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import com.zqqiliyc.framework.web.exception.ClientException;
import com.zqqiliyc.framework.web.security.SecurityUtils;
import com.zqqiliyc.framework.web.token.TokenBean;
import com.zqqiliyc.framework.web.token.TokenProvider;
import com.zqqiliyc.service.ISysTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author qili
 * @date 2025-07-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final List<AuthStrategy> authStrategies;
    private final TokenProvider tokenProvider;
    private final ISysTokenService sysTokenService;
    private final AuthManager authManager;

    public AuthResult login(LoginDto loginDto) {
        AuthStrategy strategy = CollectionUtil.findOne(authStrategies, authStrategy -> authStrategy.support(loginDto.getLoginType()));
        if (null == strategy) {
            throw new ClientException(GlobalErrorDict.UNSUPPORTED_LOGIN_TYPE);
        }
        if (log.isDebugEnabled()) {
            log.debug("will use {} for request token {}", strategy.getClass().getSimpleName(), loginDto);
        }
        AuthRequestToken requestToken = strategy.createToken(loginDto);
        return strategy.authenticate(requestToken);
    }

    @Override
    public void logout(String accessToken) {
        SecurityUtils.clearAuthentication();
        tokenProvider.revokeToken(cleanToken(accessToken));
    }

    /**
     * 获取用户信息
     *
     * @param accessToken
     * @return 用户信息
     */
    @Override
    public AuthUserInfoBean userinfo(String accessToken) {
        SysToken sysToken = sysTokenService.findByAccessToken(cleanToken(accessToken));
        return authManager.getUserInfo(sysToken.getUserId());
    }

    @Override
    public AuthResult refreshToken(String refreshToken) {
        AuthResult authResult = new AuthResult();
        TokenBean token = tokenProvider.refreshToken(refreshToken);
        if (Objects.isNull(token) || StrUtil.isBlank(token.getAccessToken())) {
            throw new ClientException(GlobalErrorDict.INVALID_TOKEN);
        }
        authResult.setAccessToken(token.getAccessToken());
        authResult.setRefreshToken(refreshToken);
        long seconds = LocalDateTimeUtil.between(token.getIssuedAt(), token.getExpiredAt()).getSeconds();
        authResult.setExpiresIn(seconds);
        return authResult;
    }

    private String cleanToken(String accessToken) {
        return accessToken.startsWith("Bearer ") ? accessToken.substring(7) : accessToken;
    }
}
