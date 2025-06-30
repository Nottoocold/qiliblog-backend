package com.zqqiliyc.auth.service.impl;

import com.zqqiliyc.auth.AuthResult;
import com.zqqiliyc.auth.dto.LoginDto;
import com.zqqiliyc.auth.enums.LoginType;
import com.zqqiliyc.auth.service.AuthStrategy;
import com.zqqiliyc.auth.token.AuthRequestToken;
import com.zqqiliyc.auth.token.EmailAuthRequestToken;
import org.springframework.stereotype.Component;

/**
 * @author qili
 * @date 2025-07-01
 */
@Component
public class EmailAuthStrategy implements AuthStrategy {

    @Override
    public boolean support(int loginType) {
        return LoginType.resolve(loginType) == LoginType.EMAIL;
    }

    @Override
    public AuthRequestToken createToken(LoginDto loginDto) {
        return new EmailAuthRequestToken(loginDto.getIdentifier(), loginDto.getCredential());
    }

    @Override
    public AuthResult authenticate(AuthRequestToken authenticationToken) {
        // 参数校验



        return null;
    }
}
