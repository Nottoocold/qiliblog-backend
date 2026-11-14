package com.zqqiliyc.module.svc.system.service.impl;

import com.zqqiliyc.framework.web.domain.dto.CreateDTO;
import com.zqqiliyc.framework.web.service.AbstractBaseService;
import com.zqqiliyc.framework.web.spring.SpringUtils;
import com.zqqiliyc.module.svc.system.config.SysTokenCacheInstanceConfig;
import com.zqqiliyc.module.svc.system.entity.SysToken;
import com.zqqiliyc.module.svc.system.mapper.SysTokenMapper;
import com.zqqiliyc.module.svc.system.service.ISysTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author qili
 * @date 2025-06-02
 */
@Slf4j
@Service
@CacheConfig(cacheNames = SysTokenCacheInstanceConfig.CACHE_NAME)
@Transactional(rollbackFor = Exception.class)
public class SysTokenService extends AbstractBaseService<SysToken, Long, SysTokenMapper> implements ISysTokenService {

    @Override
    public SysToken create(CreateDTO<SysToken> dto) {
        return SpringUtils.getBean(this.getClass()).saveToCache(super.create(dto));
    }







    /**
     * 根据accessToken查询
     *
     * @param accessToken 访问令牌
     * @return token详情
     */
    @Cacheable(key = "'ak:' + #accessToken")
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
    @Cacheable(key = "'rk:' + #refreshToken")
    @Override
    public SysToken findByRefreshToken(String refreshToken) {
        return wrapper().eq(SysToken::getRefreshToken, refreshToken).one().orElse(null);
    }

    @Override
    public void revoke(String accessToken) {
        SysToken token = SpringUtils.getBean(this.getClass()).findByAccessToken(accessToken);
        if (token != null) {
            token.setRevoked(1);
            token.setRevokedAt(LocalDateTime.now());
            baseMapper.updateByPrimaryKey(token);
            SpringUtils.getBean(this.getClass()).clearCache(token);
        }
    }

    /**
     * 每 2 小时清理无效令牌：已撤销，或刷新令牌已过期（刷新令牌恒晚于访问令牌过期）
     */
    @Scheduled(fixedRate = 2, timeUnit = TimeUnit.HOURS)
    @Override
    public void cleanToken() {
        LocalDateTime now = LocalDateTime.now();
        List<SysToken> deleted = wrapper()
                .eq(SysToken::getRevoked, 1)
                .or()
                .lt(SysToken::getRefreshExpiredAt, now)
                .list();
        if (null != deleted && !deleted.isEmpty()) {
            baseMapper.deleteByFieldList(SysToken::getId, deleted.stream().map(SysToken::getId).toList());
            SysTokenService tokenService = SpringUtils.getBean(this.getClass());
            deleted.forEach(tokenService::clearCache);
            log.info("清理无效token数量: {}", deleted.size());
        }
    }

    @Caching(put = {
            @CachePut(key = "'ak:' + #token.accessToken"),
            @CachePut(key = "'rk:' + #token.refreshToken")
    })
    public SysToken saveToCache(SysToken token) {
        if (log.isDebugEnabled()) {
            log.info("保存令牌缓存,id={}", token.getId());
        }
        return token;
    }

    @Caching(evict = {
            @CacheEvict(key = "'ak:' + #token.accessToken"),
            @CacheEvict(key = "'rk:' + #token.refreshToken")
    })
    public void clearCache(SysToken token) {
        if (log.isDebugEnabled()) {
            log.debug("清除令牌缓存,id={}", token.getId());
        }
    }
}
