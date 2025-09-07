package com.zqqiliyc.auth.strategy;

import com.zqqiliyc.auth.dto.LoginDto;
import com.zqqiliyc.auth.enums.LoginType;
import com.zqqiliyc.auth.token.AuthRequestToken;
import com.zqqiliyc.auth.token.UsernameAuthRequestToken;
import com.zqqiliyc.domain.entity.SysUser;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author qili
 * @date 2025-07-01
 */
@Component
public class UsernameAuthStrategy extends AbstractAuthStrategy {

    @Override
    public boolean support(int loginType) {
        return LoginType.resolve(loginType) == LoginType.USERNAME;
    }

    @Override
    public AuthRequestToken createToken(LoginDto loginDto) {
        return new UsernameAuthRequestToken(loginDto.getIdentifier(), loginDto.getCredential());
    }

    @Override
    protected Optional<SysUser> obtainUser(AuthRequestToken authenticationToken) {
        return authManager.findByUsername(authenticationToken.principal());
    }
}
