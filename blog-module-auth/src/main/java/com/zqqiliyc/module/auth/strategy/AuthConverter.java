package com.zqqiliyc.module.auth.strategy;

import com.zqqiliyc.module.auth.LoginDTO;
import com.zqqiliyc.module.auth.LoginType;
import org.springframework.security.core.Authentication;

/**
 * @author qili
 * @date 2025-12-24
 */
public interface AuthConverter {

    LoginType support();

    Authentication buildAuthentication(LoginDTO loginDTO);
}
