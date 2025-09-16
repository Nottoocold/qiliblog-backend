package com.zqqiliyc.auth.strategy;

import com.zqqiliyc.auth.bean.AuthResult;
import com.zqqiliyc.auth.dto.LoginDto;
import com.zqqiliyc.auth.token.AuthRequestToken;

/**
 * @author qili
 * @date 2025-07-01
 */
public interface AuthStrategy {

    /**
     * 判断是否支持该登录方式
     *
     * @param loginType 登录方式
     * @return true 支持，false 不支持
     * @see com.zqqiliyc.auth.enums.LoginType
     */
    boolean support(int loginType);

    /**
     * 将提交的登录信息转换为认证请求token
     *
     * @param loginDto 登录信息
     * @return 认证请求token
     */
    AuthRequestToken createToken(LoginDto loginDto);

    /**
     * 认证
     *
     * @param authenticationToken 认证请求token
     * @return 认证结果
     */
    AuthResult authenticate(AuthRequestToken authenticationToken);
}
