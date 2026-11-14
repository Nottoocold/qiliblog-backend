package com.zqqiliyc.service.token;

import cn.hutool.core.util.RandomUtil;
import com.zqqiliyc.BaseTestApp;
import com.zqqiliyc.framework.web.enums.TokenStyle;
import com.zqqiliyc.module.svc.system.dto.token.SysTokenCreateDTO;
import com.zqqiliyc.module.svc.system.entity.SysToken;
import com.zqqiliyc.module.svc.system.service.ISysTokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 令牌持久化的表征测试：穿过 ISysTokenService 断言创建/查询/撤销/清理的可观察行为
 */
public class SysTokenServiceTest extends BaseTestApp {
    @Autowired
    ISysTokenService tokenService;

    @Test
    @Order(0)
    public void testCreateFindRevoke() {
        SysTokenCreateDTO dto = baseDto();
        SysToken created = tokenService.create(dto);
        Assertions.assertNotNull(created.getId());

        // 双索引查询命中同一条记录，additionalInfo 经 JSON typehandler 往返
        SysToken byAccessToken = tokenService.findByAccessToken(dto.getAccessToken());
        SysToken byRefreshToken = tokenService.findByRefreshToken(dto.getRefreshToken());
        Assertions.assertEquals(created.getId(), byAccessToken.getId());
        Assertions.assertEquals(created.getId(), byRefreshToken.getId());
        Assertions.assertEquals(Map.of("role", "USER"), byAccessToken.getAdditionalInfo());

        tokenService.revoke(dto.getAccessToken());
        SysToken revoked = tokenService.findByAccessToken(dto.getAccessToken());
        Assertions.assertEquals(1, revoked.getRevoked());
        Assertions.assertNotNull(revoked.getRevokedAt());

        // 已撤销的令牌被清理，两个缓存键同时失效
        tokenService.cleanToken();
        Assertions.assertNull(tokenService.findByAccessToken(dto.getAccessToken()));
        Assertions.assertNull(tokenService.findByRefreshToken(dto.getRefreshToken()));
    }

    @Test
    @Order(1)
    public void testCleanTokenSemantics() {
        LocalDateTime now = LocalDateTime.now();
        // 未过期
        SysTokenCreateDTO valid = baseDto();
        valid.setExpiredAt(now.plusHours(1));
        valid.setRefreshExpiredAt(now.plusHours(6));
        // 访问令牌已过期，刷新令牌仍有效
        SysTokenCreateDTO accessExpired = baseDto();
        accessExpired.setExpiredAt(now.minusHours(1));
        accessExpired.setRefreshExpiredAt(now.plusHours(6));
        // 访问令牌与刷新令牌都已过期
        SysTokenCreateDTO fullyExpired = baseDto();
        fullyExpired.setExpiredAt(now.minusHours(6));
        fullyExpired.setRefreshExpiredAt(now.minusHours(1));
        // 未过期但已撤销
        SysTokenCreateDTO revoked = baseDto();
        revoked.setRevoked(1);
        revoked.setRevokedAt(now);

        SysToken validToken = tokenService.create(valid);
        SysToken accessExpiredToken = tokenService.create(accessExpired);
        SysToken fullyExpiredToken = tokenService.create(fullyExpired);
        SysToken revokedToken = tokenService.create(revoked);

        tokenService.cleanToken();

        // 清理判据：已撤销 或 刷新令牌已过期；用 findById 直读数据库，避开令牌缓存遮蔽
        Assertions.assertNotNull(tokenService.findById(validToken.getId()));
        Assertions.assertNotNull(tokenService.findById(accessExpiredToken.getId()));
        Assertions.assertNull(tokenService.findById(fullyExpiredToken.getId()));
        Assertions.assertNull(tokenService.findById(revokedToken.getId()));

        // 清理存活令牌：撤销后清理，顺带走一遍缓存失效路径
        tokenService.revoke(valid.getAccessToken());
        tokenService.revoke(accessExpired.getAccessToken());
        tokenService.cleanToken();
        Assertions.assertNull(tokenService.findById(validToken.getId()));
        Assertions.assertNull(tokenService.findById(accessExpiredToken.getId()));
    }

    private SysTokenCreateDTO baseDto() {
        SysTokenCreateDTO dto = new SysTokenCreateDTO();
        dto.setAccessToken(generateString(3, () -> RandomUtil.randomString(16)));
        dto.setRefreshToken(generateString(3, () -> RandomUtil.randomString(16)));
        dto.setTokenStyle(TokenStyle.JWT);
        dto.setUserId(1L);
        LocalDateTime now = LocalDateTime.now();
        dto.setIssuedAt(now);
        dto.setExpiredAt(now.plusHours(1));
        dto.setRefreshExpiredAt(now.plusHours(6));
        dto.setIpAddress("127.0.0.1");
        dto.setAdditionalInfo(Map.of("role", "USER"));
        return dto;
    }
}
