package com.zqqiliyc.auth.service;

import com.zqqiliyc.auth.AuthResult;
import com.zqqiliyc.auth.dto.LoginDto;

/**
 * @author qili
 * @date 2025-07-01
 */
public interface IAuthService {

    /**
     * 登录
     *
     * @param loginDto 登录信息
     * @return 登录结果
     */
    AuthResult login(LoginDto loginDto);

    /**
     * 登出
     *
     * @param accessToken 访问令牌
     */
    void logout(String accessToken);
}
