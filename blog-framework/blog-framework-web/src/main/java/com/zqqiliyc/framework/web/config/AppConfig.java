package com.zqqiliyc.framework.web.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.zqqiliyc.framework.web.config.prop.TokenProperties;
import com.zqqiliyc.framework.web.config.prop.VerificationProperties;
import com.zqqiliyc.framework.web.strategy.VerificationCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author qili
 * @date 2025-07-17
 */
@Slf4j
@Configuration
@EnableAsync
@EnableScheduling
@EnableAspectJAutoProxy
@EnableConfigurationProperties({TokenProperties.class, VerificationProperties.class})
public class AppConfig {

    @Bean
    public static VerificationCacheService verificationCacheService(VerificationProperties verificationProperties) {
        final var properties = verificationProperties;
        Cache<Object, Object> cache = Caffeine.newBuilder()
                .maximumSize(1024).recordStats()
                .expireAfter(new Expiry<>() {
                    @Override
                    public long expireAfterCreate(Object key, Object value, long currentTime) {
                        // 验证码创建后固定缓存有效期
                        if (log.isDebugEnabled()) {
                            log.debug("Verification code created, key: {}, value: {}", key, value);
                        }
                        return TimeUnit.SECONDS.toNanos(properties.getExpirationSeconds());
                    }

                    @Override
                    public long expireAfterUpdate(Object key, Object value, long currentTime, long currentDuration) {
                        // 验证码更新后重置有效期
                        // 即每次重新发生验证码时都应该重置有效期，无论新验证码与旧验证码是否相同
                        if (log.isDebugEnabled()) {
                            log.debug("Verification code updated, will rest expiredTime, key: {}, value: {}", key, value);
                        }
                        return TimeUnit.SECONDS.toNanos(properties.getExpirationSeconds());
                    }

                    @Override
                    public long expireAfterRead(Object key, Object value, long currentTime, long currentDuration) {
                        // 验证码读取后有效期不变
                        return currentDuration;
                    }
                })
                .removalListener((key, value, cause) -> {
                    if (log.isDebugEnabled()) {
                        log.debug("Verification code removed, key: {}, value: {} cause: {}", key, value, cause);
                    }
                })
                .build();

        CaffeineCache caffeineCache = new CaffeineCache("verification_code_cache", cache);
        return new VerificationCacheService(caffeineCache);
    }

    @Bean
    public static ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        executor.setMaxPoolSize(executor.getCorePoolSize() * 2);
        executor.setQueueCapacity(executor.getMaxPoolSize() * 10);
        executor.setThreadNamePrefix("TaskExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }
}
