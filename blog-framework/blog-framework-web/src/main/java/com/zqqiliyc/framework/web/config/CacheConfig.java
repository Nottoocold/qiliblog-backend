package com.zqqiliyc.framework.web.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * @author qili
 * @date 2025-09-07
 */
@Configuration
@EnableCaching
public class CacheConfig extends BaseJacksonConfig {

    /**
     * Redis缓存管理器
     */
    /*@Bean
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))
                // 设置key的序列化方式为String
                .serializeKeysWith(RedisSerializationContext
                        .SerializationPair.fromSerializer(RedisSerializer.string()))
                // 设置Value序列化方式为Json
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair.fromSerializer(createJacksonSerializerForCacheUse(objectMapper)))
                // 禁止缓存null值，避免缓存穿透（按需设置）
                .disableCachingNullValues()
                // 缓存名称前缀
                .computePrefixWith(cacheName -> cacheName + ":");
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(config)
                .build();
    }*/
}
