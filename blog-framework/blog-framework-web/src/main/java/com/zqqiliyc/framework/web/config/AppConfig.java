package com.zqqiliyc.framework.web.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.zqqiliyc.framework.web.config.prop.TokenProperties;
import com.zqqiliyc.framework.web.config.prop.VerificationProperties;
import com.zqqiliyc.framework.web.strategy.VerificationCacheService;
import org.checkerframework.checker.index.qual.NonNegative;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.TimeUnit;

/**
 * @author qili
 * @date 2025-07-17
 */
@Configuration
@EnableScheduling
@EnableConfigurationProperties({TokenProperties.class, VerificationProperties.class})
public class AppConfig {

    @Bean
    public VerificationCacheService verificationCacheService(VerificationProperties verificationProperties) {
        Cache<Object, Object> cache = Caffeine.newBuilder()
                .expireAfter(new Expiry<>() {
                    @Override
                    public long expireAfterCreate(Object key, Object value, long currentTime) {
                        // 缓存创建后固定缓存有效期
                        return TimeUnit.SECONDS.toNanos(verificationProperties.getExpirationSeconds());
                    }

                    @Override
                    public long expireAfterUpdate(Object key, Object value, long currentTime, @NonNegative long currentDuration) {
                        // 缓存更新后有效期不变
                        return currentDuration;
                    }

                    @Override
                    public long expireAfterRead(Object key, Object value, long currentTime, @NonNegative long currentDuration) {
                        // 缓存读取后有效期不变
                        return currentDuration;
                    }
                })
                .build();

        CaffeineCache caffeineCache = new CaffeineCache("verification_code_cache", cache);
        return new VerificationCacheService(caffeineCache);
    }
}
