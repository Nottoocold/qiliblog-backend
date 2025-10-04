package com.zqqiliyc.biz.core.service.impl;

import com.zqqiliyc.biz.core.entity.SysToken;
import com.zqqiliyc.biz.core.repository.mapper.SysTokenMapper;
import com.zqqiliyc.biz.core.service.ISysTokenService;
import com.zqqiliyc.biz.core.service.base.AbstractBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author qili
 * @date 2025-06-02
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysTokenService extends AbstractBaseService<SysToken, Long, SysTokenMapper> implements ISysTokenService {

    /**
     * 根据accessToken查询
     *
     * @param accessToken 访问令牌
     * @return token详情
     */
    @Override
    public SysToken findByAccessToken(String accessToken) {
        return wrapper().eq(SysToken::getAccessToken, accessToken).one().orElse(null);
    }

    /**
     * 根据refreshToken查询
     *
     * @param refreshToken 刷新令牌
     * @return token详情
     */
    @Override
    public SysToken findByRefreshToken(String refreshToken) {
        return wrapper().eq(SysToken::getRefreshToken, refreshToken).one().orElse(null);
    }

    /**
     * 根据token查询, 包含accessToken和refreshToken
     *
     * @param token 令牌
     * @return token详情
     */
    @Override
    public SysToken findByToken(String token) {
        return wrapper()
                .eq(SysToken::getAccessToken, token)
                .or()
                .eq(SysToken::getRefreshToken, token).one().orElse(null);
    }

    @Override
    public void revoke(String accessToken) {
        SysToken token = findByAccessToken(accessToken);
        if (token != null) {
            token.setRevoked(1);
            token.setRevokedAt(LocalDateTime.now());
            update(token);
        }
    }

    @Override
    public void cleanToken() {
        LocalDateTime now = LocalDateTime.now();
        int deleted = wrapper()
                .eq(SysToken::getRevoked, 1)
                .or()
                .lt(SysToken::getExpiredAt, now)
                .lt(SysToken::getRefreshExpiredAt, now)
                .or()
                .lt(SysToken::getRefreshExpiredAt, now)
                .delete();
        log.info("清楚无效token数量: {}", deleted);
    }
}
