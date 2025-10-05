package com.zqqiliyc.framework.web.config.cache;

import org.springframework.cache.Cache;

/**
 * @author qili
 * @date 2025-10-05
 */
public interface CacheInstanceConfig {

    /**
     * 缓存名称
     *
     * @return 缓存名称
     */
    String getCacheName();

    /**
     * 缓存描述
     *
     * @return 缓存描述
     */
    String getCacheDescription();

    /**
     * 获取缓存实例
     *
     * @return 缓存实例
     */
    Cache getCache();
}
