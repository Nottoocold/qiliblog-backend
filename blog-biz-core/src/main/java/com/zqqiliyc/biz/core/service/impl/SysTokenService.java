package com.zqqiliyc.biz.core.service.impl;

import com.zqqiliyc.biz.core.dto.CreateDto;
import com.zqqiliyc.biz.core.entity.SysToken;
import com.zqqiliyc.biz.core.repository.mapper.SysTokenMapper;
import com.zqqiliyc.biz.core.service.ISysTokenService;
import com.zqqiliyc.biz.core.service.base.AbstractBaseService;
import com.zqqiliyc.framework.web.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author qili
 * @date 2025-06-02
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "system:token")
@Transactional(rollbackFor = Exception.class)
public class SysTokenService extends AbstractBaseService<SysToken, Long, SysTokenMapper> implements ISysTokenService {

    @Caching(put = {
            @CachePut(key = "'ak:' + #dto.toEntity().accessToken"),
            @CachePut(key = "'rk:' + #dto.toEntity().refreshToken")
    })
    @Override
    public SysToken create(CreateDto<SysToken> dto) {
        return super.create(dto);
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

    @Override
    public void cleanToken() {
        LocalDateTime now = LocalDateTime.now();
        List<SysToken> deleted = wrapper()
                .eq(SysToken::getRevoked, 1)
                .or()
                .lt(SysToken::getExpiredAt, now)
                .lt(SysToken::getRefreshExpiredAt, now)
                .or()
                .lt(SysToken::getRefreshExpiredAt, now)
                .list();
        if (null != deleted && !deleted.isEmpty()) {
            baseMapper.deleteByFieldList(SysToken::getId, deleted.stream().map(SysToken::getId).toList());
            SysTokenService tokenService = SpringUtils.getBean(this.getClass());
            deleted.forEach(tokenService::clearCache);
            log.info("清楚无效token数量: {}", deleted.size());
        }
    }

    @Caching(evict = {
            @CacheEvict(key = "'ak:' + #token.accessToken"),
            @CacheEvict(key = "'rk:' + #token.refreshToken")
    })
    public void clearCache(SysToken token) {
        if (log.isDebugEnabled()) {
            log.debug("清除ID={}的SysToken缓存", token.getId());
        }
    }
}
