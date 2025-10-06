package com.zqqiliyc.biz.core.config.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zqqiliyc.framework.web.config.cache.CaffeineCacheInstanceConfig;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author qili
 * @date 2025-10-05
 */
@Component
public class SysTokenCacheInstanceConfig implements CaffeineCacheInstanceConfig {
    public static final String CACHE_NAME = "sysTokenCache";
    private final Cache<Object, Object> nativeCache;

    public SysTokenCacheInstanceConfig() {
        this.nativeCache = Caffeine.newBuilder()
                .maximumSize(128)
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }

    @Override
    public String getCacheName() {
        return CACHE_NAME;
    }

    @Override
    public String getCacheDescription() {
        return "系统令牌缓存";
    }

    @Override
    public Cache<Object, Object> getCaffeineCache() {
        return nativeCache;
    }
}
