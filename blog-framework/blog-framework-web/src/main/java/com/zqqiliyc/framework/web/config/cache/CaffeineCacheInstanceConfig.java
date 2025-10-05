package com.zqqiliyc.framework.web.config.cache;

import com.github.benmanes.caffeine.cache.Cache;

/**
 * @author qili
 * @date 2025-10-05
 */
public interface CaffeineCacheInstanceConfig extends CacheInstanceConfig {

    /**
     * 获取Caffeine缓存实例
     *
     * @return Caffeine缓存实例
     */
    Cache<Object, Object> getCaffeineCache();
}
