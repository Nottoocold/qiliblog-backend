package com.zqqiliyc.auth.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.zqqiliyc.auth.AuthResult;
import com.zqqiliyc.auth.dto.LoginDto;
import com.zqqiliyc.auth.service.IAuthService;
import com.zqqiliyc.auth.strategy.AuthStrategy;
import com.zqqiliyc.auth.token.AuthRequestToken;
import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import com.zqqiliyc.framework.web.exception.ClientException;
import com.zqqiliyc.framework.web.security.SecurityUtils;
import com.zqqiliyc.framework.web.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
        tokenProvider.revokeToken(accessToken);
    }
}
