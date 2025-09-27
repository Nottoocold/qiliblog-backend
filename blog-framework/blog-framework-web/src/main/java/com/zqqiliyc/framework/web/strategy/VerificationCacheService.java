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

    private String getVerificationCode(String key) {
        return cache.get(key, String.class);
    }

    private void removeVerificationCode(String key) {
        cache.evict(key);
    }

    /**
     * 验证验证码, 验证成功则删除缓存
     *
     * @param key  缓存的key
     * @param code 验证码
     * @return 验证码验证成功返回true，否则返回false
     */
    public boolean verifyCode(String key, String code) {
        String storedCode = getVerificationCode(key);
        if (storedCode != null && storedCode.equals(code)) {
            // 验证码验证成功，删除缓存
            removeVerificationCode(key);
            return true;
        }
        return false;
    }
}
