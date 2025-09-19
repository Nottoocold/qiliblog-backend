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
     * 使用userId和自定义信息生成token
     *
     * @param userId 与之关联的用户ID
     * @param claims 自定义的token信息
     * @return 生成的token
     */
    TokenBean generateToken(Long userId, Map<String, Object> claims);

    /**
     * 刷新token, 刷新成功的条件：<br/>
     * 1. refreshToken有效(刷新令牌本身有效)<br/>
     * 刷新成功以后执行的操作：<br/>
     * 1. 撤销旧token
     *
     * @param refreshToken refreshToken
     * @return 生成的token
     */
    TokenBean refreshToken(String refreshToken);

    /**
     * 撤销token
     *
     * @param accessToken accessToken
     */
    void revokeToken(String accessToken);

    /**
     * 校验 accessToken，accessToken有效条件：<br/>
     * 1. 签名验证通过<br/>
     * 2. 令牌没有被撤销
     *
     * @param accessToken accessToken
     * @return true if valid, false otherwise
     */
    boolean verifyAccessToken(String accessToken);

    /**
     * 校验 refreshToken，refreshToken有效条件：<br/>
     * 1. 签名验证通过<br/>
     * 2. 令牌没有被撤销
     *
     * @param refreshToken refreshToken
     * @return true if valid, false otherwise
     */
    boolean verifyRefreshToken(String refreshToken);

    /**
     * 获取token的附加信息
     *
     * @param token token
     * @return claims
     */
    Map<String, Object> getClaims(String token);
}
