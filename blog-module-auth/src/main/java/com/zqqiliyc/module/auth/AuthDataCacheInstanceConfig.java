package com.zqqiliyc.module.auth;

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
public class AuthDataCacheInstanceConfig implements CaffeineCacheInstanceConfig {
    public static final String CACHE_NAME = "authUserDataCache";
    private final Cache<Object, Object> nativeCache;

    public AuthDataCacheInstanceConfig() {
        this.nativeCache = Caffeine.newBuilder()
                .maximumSize(128)
                .expireAfterAccess(1, TimeUnit.HOURS)
                .recordStats()
                .build();
    }

    @Override
    public Cache<Object, Object> getCaffeineCache() {
        return nativeCache;
    }

    @Override
    public String getCacheName() {
        return CACHE_NAME;
    }

    @Override
    public String getCacheDescription() {
        return "用户权限数据缓存";
    }
}
