package com.zqqiliyc.auth.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.zqqiliyc.auth.AuthResult;
import com.zqqiliyc.auth.dto.LoginDto;
import com.zqqiliyc.auth.service.AuthStrategy;
import com.zqqiliyc.auth.service.ILoginService;
import com.zqqiliyc.auth.token.AuthRequestToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qili
 * @date 2025-07-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService implements ILoginService {
    private final List<AuthStrategy> authStrategies;

    public AuthResult login(LoginDto loginDto) {
        AuthStrategy strategy = CollectionUtil.findOne(authStrategies, authStrategy -> authStrategy.support(loginDto.getLoginType()));
        if (null == strategy) {
            return new AuthResult(0, false, "不支持的登录方式");
        }
        if (log.isDebugEnabled()) {
            log.debug("will use {} authenticator with {}", strategy.getClass().getName(), loginDto);
        }
        AuthRequestToken requestToken = strategy.createToken(loginDto);
        return strategy.authenticate(requestToken);
    }
}
