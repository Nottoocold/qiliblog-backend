package com.zqqiliyc.auth.service;

import com.zqqiliyc.auth.AuthResult;
import com.zqqiliyc.auth.dto.LoginDto;

/**
 * @author qili
 * @date 2025-07-01
 */
public interface ILoginService {

    AuthResult login(LoginDto loginDto);
}
