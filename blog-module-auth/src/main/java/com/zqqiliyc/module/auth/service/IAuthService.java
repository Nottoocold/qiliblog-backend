package com.zqqiliyc.module.auth.service;

import com.zqqiliyc.framework.web.bean.AuthUserInfoBean;
import com.zqqiliyc.module.auth.AuthResult;
import com.zqqiliyc.module.auth.LoginDTO;

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
    AuthResult login(LoginDTO loginDto);

    /**
     * 登出：撤销当前认证所用的令牌并清除认证上下文，用户与令牌均取自认证结果
     */
    void logout();

    /**
     * 获取当前认证用户的信息
     *
     * @return 用户信息
     */
    AuthUserInfoBean userinfo();

    /**
     * 刷新令牌
     *
     * @param refreshToken 刷新令牌
     * @return 刷新结果
     */
    AuthResult refreshToken(String refreshToken);
}
