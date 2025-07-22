package com.zqqiliyc.framework.web.spring;

import cn.hutool.core.util.ArrayUtil;
import jakarta.annotation.Nonnull;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author zqqiliyc
 * @since 2025-07-21
 */
@Component
public class SpringEnvUtils implements EnvironmentAware {
    private static Environment environment;

    @Override
    public void setEnvironment(@Nonnull Environment environment) {
        SpringEnvUtils.environment = environment;
    }

    public static String getEnv(String key) {
        return environment.getProperty(key);
    }

    public static String getEnv(String key, String defaultValue) {
        return environment.getProperty(key, defaultValue);
    }

    public static <T> T getEnv(String key, Class<T> clazz) {
        return environment.getProperty(key, clazz);
    }

    public static <T> T getEnv(String key, Class<T> clazz, T defaultValue) {
        return environment.getProperty(key, clazz, defaultValue);
    }

    public static String[] getProfiles() {
        return environment.getActiveProfiles();
    }

    public static boolean isDev() {
        return ArrayUtil.contains(getProfiles(), "dev");
    }

    public static boolean isProd() {
        return ArrayUtil.contains(getProfiles(), "prod");
    }

    public static boolean isTest() {
        return ArrayUtil.contains(getProfiles(), "test");
    }

    public static boolean isProfile(String profile) {
        return ArrayUtil.contains(getProfiles(), profile);
    }

    public static boolean isProfiles(String... profiles) {
        return ArrayUtil.containsAll(getProfiles(), profiles);
    }
}
