package com.zqqiliyc.auth.strategy;

import com.zqqiliyc.auth.AuthResult;
import com.zqqiliyc.auth.dto.LoginDto;
import com.zqqiliyc.auth.token.AuthRequestToken;

/**
 * @author qili
 * @date 2025-07-01
 */
public interface AuthStrategy {

    boolean support(int loginType);

    AuthRequestToken createToken(LoginDto loginDto);

    AuthResult authenticate(AuthRequestToken authenticationToken);
}
