package com.zqqiliyc.service.auth;

import com.zqqiliyc.framework.web.bean.AuthUserInfoBean;
import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import com.zqqiliyc.framework.web.exception.ClientException;
import com.zqqiliyc.framework.web.security.SecurityUtils;
import com.zqqiliyc.module.auth.AuthDataCacheInstanceConfig;
import com.zqqiliyc.module.auth.service.IAuthService;
import com.zqqiliyc.module.auth.token.JwtAuthenticationToken;
import com.zqqiliyc.module.svc.system.entity.SysToken;
import com.zqqiliyc.module.svc.system.service.ISysTokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * 登出与用户信息的行为测试：两者只消费认证结果，令牌撤销与缓存清除可观察，
 * 缺少认证时按客户端错误处理而不是空指针
 */
public class AuthServiceTest extends BaseAuthUserTest {
    @Autowired
    IAuthService authService;
    @Autowired
    ISysTokenService tokenService;
    @Autowired
    CacheManager cacheManager;

    @Test
    @Order(0)
    public void testLogoutRevokesTokenAndClearsUserCache() {
        String token = issueAccessToken();
        authenticateAs(token);
        Assertions.assertNotNull(userInfoCache().get(userInfoCacheKey()), "登出前用户缓存应已预热");

        authService.logout();

        SysToken stored = tokenService.findByAccessToken(token);
        Assertions.assertNotNull(stored);
        Assertions.assertEquals(1, stored.getRevoked());
        Assertions.assertNotNull(stored.getRevokedAt());
        Assertions.assertNull(SecurityUtils.getAuthentication(), "登出应清空认证上下文");
        Assertions.assertNull(userInfoCache().get(userInfoCacheKey()), "登出应清除用户权限缓存");
    }

    @Test
    @Order(1)
    public void testUserinfoReturnsAuthenticatedUser() {
        authenticateAs(issueAccessToken());

        AuthUserInfoBean userInfo = authService.userinfo();

        Assertions.assertEquals(userId, userInfo.getId());
    }

    @Test
    @Order(2)
    public void testLogoutWithoutAuthenticationThrowsClientException() {
        SecurityUtils.clearAuthentication();

        ClientException exception = Assertions.assertThrowsExactly(ClientException.class, () -> authService.logout());

        Assertions.assertEquals(GlobalErrorDict.NOT_AUTHENTICATED.getCode(), exception.getStatus());
    }

    @Test
    @Order(3)
    public void testUserinfoWithoutAuthenticationThrowsClientException() {
        SecurityUtils.clearAuthentication();

        ClientException exception = Assertions.assertThrowsExactly(ClientException.class, () -> authService.userinfo());

        Assertions.assertEquals(GlobalErrorDict.NOT_AUTHENTICATED.getCode(), exception.getStatus());
    }

    /**
     * 模拟过滤器认证之后的上下文：principal 是用户信息，credentials 是原始令牌串
     *
     * @param accessToken 已签发的访问令牌
     */
    private void authenticateAs(String accessToken) {
        SecurityUtils.setAuthentication(JwtAuthenticationToken.authenticated(authManager.getUserInfo(userId), accessToken));
    }

    private Cache userInfoCache() {
        return cacheManager.getCache(AuthDataCacheInstanceConfig.CACHE_NAME);
    }

    /**
     * 与 AuthManager 上 @Cacheable 的键一致
     *
     * @return 用户信息缓存键
     */
    private String userInfoCacheKey() {
        return "loginSession:userinfo:" + userId;
    }
}
