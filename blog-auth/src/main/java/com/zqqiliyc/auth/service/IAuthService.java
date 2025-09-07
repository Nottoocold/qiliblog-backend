package com.zqqiliyc.auth.service;

import com.zqqiliyc.auth.AuthResult;
import com.zqqiliyc.auth.dto.LoginDto;
import com.zqqiliyc.framework.web.bean.AuthUserInfoBean;

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

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    AuthUserInfoBean userinfo(String accessToken);

    /**
     * 刷新令牌
     *
     * @param refreshToken 刷新令牌
     * @return 刷新结果
     */
    AuthResult refreshToken(String refreshToken);
}
