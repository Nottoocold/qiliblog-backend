package com.zqqiliyc.service;

import com.zqqiliyc.domain.entity.SysToken;
import com.zqqiliyc.service.base.IBaseService;

/**
 * @author qili
 * @date 2025-06-02
 */
public interface ISysTokenService extends IBaseService<SysToken, Long> {

    /**
     * 根据accessToken查询
     *
     * @param accessToken 访问令牌
     * @return token详情
     */
    SysToken findByAccessToken(String accessToken);

    /**
     * 根据refreshToken查询
     *
     * @param refreshToken 刷新令牌
     * @return token详情
     */
    SysToken findByRefreshToken(String refreshToken);

    /**
     * 根据token查询, 包含accessToken和refreshToken
     *
     * @param token 令牌
     * @return token详情
     */
    SysToken findByToken(String token);

    /**
     * 撤销
     *
     * @param accessToken 访问令牌
     */
    void revoke(String accessToken);

    /**
     * 清理无效的token
     */
    void cleanToken();
}
