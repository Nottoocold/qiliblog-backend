package com.zqqiliyc.framework.web.strategy;

import org.springframework.cache.Cache;

/**
 * @author qili
 * @date 2025-09-27
 */
public record VerificationCacheService(Cache cache) {

    public void storeVerificationCode(String key, String code) {
        cache.put(key, code);
    }

    public String getVerificationCode(String key) {
        return cache.get(key, String.class);
    }

    public void removeVerificationCode(String key) {
        cache.evict(key);
    }

    public boolean verifyCode(String key, String code) {
        String storedCode = getVerificationCode(key);
        return storedCode != null && storedCode.equals(code);
    }
}
