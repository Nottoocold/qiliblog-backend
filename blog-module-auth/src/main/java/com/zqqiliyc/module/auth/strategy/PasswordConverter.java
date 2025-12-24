package com.zqqiliyc.module.auth.strategy;

import com.zqqiliyc.module.auth.LoginDTO;
import com.zqqiliyc.module.auth.LoginType;
import com.zqqiliyc.module.auth.token.PasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * @author qili
 * @date 2025-12-24
 */
@Component
public class PasswordConverter implements AuthConverter {
    @Override
    public LoginType support() {
        return LoginType.USERNAME;
    }

    @Override
    public Authentication buildAuthentication(LoginDTO loginDTO) {
        return PasswordAuthenticationToken.unauthenticated(loginDTO.getIdentifier(), loginDTO.getCredential());
    }
}
