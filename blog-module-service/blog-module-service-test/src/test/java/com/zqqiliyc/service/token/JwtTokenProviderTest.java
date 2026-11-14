package com.zqqiliyc.service.token;

import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.BaseTestApp;
import com.zqqiliyc.framework.web.token.TokenBean;
import com.zqqiliyc.framework.web.token.TokenProvider;
import com.zqqiliyc.module.svc.system.entity.SysToken;
import com.zqqiliyc.module.svc.system.service.ISysTokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 令牌生命周期的表征测试：穿过 TokenProvider 断言生成/校验/刷新/撤销的可观察行为，落库副作用由 ISysTokenService 核对
 */
public class JwtTokenProviderTest extends BaseTestApp {
    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    ISysTokenService tokenService;

    @Test
    @Order(0)
    public void testTokenLifecycle() {
        TokenBean generated = tokenProvider.generateToken(1L, Map.of("role", "USER"));
        // 同秒连续签发的令牌必须唯一（jti 保证），否则撞 access_token 唯一索引
        TokenBean second = tokenProvider.generateToken(1L, Map.of("role", "USER"));
        Assertions.assertNotEquals(generated.getAccessToken(), second.getAccessToken());
        Assertions.assertTrue(tokenProvider.verifyAccessToken(second.getAccessToken()));
        Assertions.assertTrue(StrUtil.isNotBlank(generated.getAccessToken()));
        Assertions.assertTrue(StrUtil.isNotBlank(generated.getRefreshToken()));
        Assertions.assertTrue(tokenProvider.verifyAccessToken(generated.getAccessToken()));
        Assertions.assertTrue(tokenProvider.verifyRefreshToken(generated.getRefreshToken()));

        // 生成即落库
        SysToken stored = tokenService.findByAccessToken(generated.getAccessToken());
        Assertions.assertNotNull(stored);
        Assertions.assertEquals(1L, stored.getUserId());
        Assertions.assertEquals(generated.getRefreshToken(), stored.getRefreshToken());
        Assertions.assertEquals(Map.of("role", "USER"), stored.getAdditionalInfo());
        // web 请求内签发时记录来源 IP
        Assertions.assertTrue(StrUtil.isNotBlank(stored.getIpAddress()));


        // 刷新：新令牌对有效，旧令牌对整体失效
        TokenBean refreshed = tokenProvider.refreshToken(generated.getRefreshToken());
        Assertions.assertNotNull(refreshed);
        Assertions.assertTrue(tokenProvider.verifyAccessToken(refreshed.getAccessToken()));
        Assertions.assertTrue(tokenProvider.verifyRefreshToken(refreshed.getRefreshToken()));
        Assertions.assertFalse(tokenProvider.verifyAccessToken(generated.getAccessToken()));
        Assertions.assertFalse(tokenProvider.verifyRefreshToken(generated.getRefreshToken()));

        tokenProvider.revokeToken(refreshed.getAccessToken());
        Assertions.assertFalse(tokenProvider.verifyAccessToken(refreshed.getAccessToken()));
        tokenProvider.revokeToken(second.getAccessToken());

        // 附加信息可从令牌本身取回，不依赖落库记录
        Assertions.assertEquals("USER", tokenProvider.getClaims(refreshed.getAccessToken()).get("role"));

        // 签名被篡改的令牌校验不通过
        String[] parts = refreshed.getAccessToken().split("\\.");
        String tampered = parts[0] + "." + parts[1] + "." + new StringBuilder(parts[2]).reverse();
        Assertions.assertFalse(tokenProvider.verifyAccessToken(tampered));

        tokenService.cleanToken();
        Assertions.assertNull(tokenService.findByAccessToken(generated.getAccessToken()));
        Assertions.assertNull(tokenService.findByAccessToken(refreshed.getAccessToken()));
    }
}
