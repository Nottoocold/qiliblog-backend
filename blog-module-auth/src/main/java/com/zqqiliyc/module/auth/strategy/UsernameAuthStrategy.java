package com.zqqiliyc.module.auth.strategy;

import com.zqqiliyc.module.auth.dto.LoginDTO;
import com.zqqiliyc.module.auth.enums.LoginType;
import com.zqqiliyc.module.auth.token.AuthRequestToken;
import com.zqqiliyc.module.auth.token.UsernameAuthRequestToken;
import com.zqqiliyc.module.biz.entity.SysUser;
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
    public AuthRequestToken createToken(LoginDTO loginDto) {
        return new UsernameAuthRequestToken(loginDto.getIdentifier(), loginDto.getCredential());
    }

    @Override
    protected Optional<SysUser> obtainUser(AuthRequestToken authenticationToken) {
        return authManager.findByUsername(authenticationToken.principal());
    }
}
