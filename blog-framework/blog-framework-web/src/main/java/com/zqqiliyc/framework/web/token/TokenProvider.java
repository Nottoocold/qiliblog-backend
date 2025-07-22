package com.zqqiliyc.framework.web.token;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisherAware;

import java.util.Map;

/**
 * token provider
 *
 * @author qili
 * @date 2025-07-13
 */
public interface TokenProvider extends ApplicationEventPublisherAware, InitializingBean {

    /**
     * Generate token, use userId and claims to generate token
     *
     * @param userId 与之关联的用户ID
     * @param claims 自定义的token信息
     * @return 生成的token
     */
    TokenBean generateToken(Long userId, Map<String, Object> claims);

    /**
     * Refresh token, use refreshToken to refresh token<br/>
     * only valid token can be refreshed
     *
     * @param refreshToken refreshToken
     * @return 生成的token
     */
    TokenBean refreshToken(String refreshToken);

    /**
     * Revoke token, use accessToken to revoke token<br/>
     * only valid token can be revoked
     *
     * @param accessToken accessToken
     */
    void revokeToken(String accessToken);

    /**
     * validate token
     *
     * @param accessToken accessToken
     * @return true if valid, false otherwise
     */
    boolean validateToken(String accessToken);

    /**
     * Get claims from token
     *
     * @param accessToken accessToken
     * @return claims
     */
    Map<String, Object> getClaims(String accessToken);
}
