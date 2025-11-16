package com.zqqiliyc.framework.web.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.zqqiliyc.framework.web.config.cache.CaffeineCacheInstanceConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

/**
 * @author qili
 * @date 2025-09-07
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableCaching
public class CacheConfig extends BaseJacksonConfig {

    @Bean("caffeineCacheManager")
    public CaffeineCacheManager caffeineCacheManager(List<CaffeineCacheInstanceConfig> cacheInstanceConfigs) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        // 默认缓存构建配置
        Caffeine<Object, Object> defaultCacheBuilder = Caffeine.newBuilder()
                .maximumSize(512)
                .expireAfterAccess(Duration.ofMinutes(3))
                .recordStats();
        cacheManager.setCaffeine(defaultCacheBuilder);
        if (cacheInstanceConfigs != null) {
            for (CaffeineCacheInstanceConfig cacheInstanceConfig : cacheInstanceConfigs) {
                // 注册自定义的缓存实例，用于不同场景下使用不同的缓存配置
                cacheManager.registerCustomCache(cacheInstanceConfig.getCacheName(),
                        cacheInstanceConfig.getCaffeineCache());
            }
        }
        return cacheManager;
    }

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
