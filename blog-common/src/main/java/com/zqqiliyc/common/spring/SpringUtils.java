package com.zqqiliyc.common.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author zqqiliyc
 * @since 2025-07-22
 */
@Component
public class SpringUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    public static String[] getBeanNames(Class<?> clazz) {
        return applicationContext.getBeanNamesForType(clazz);
    }

    public static void publishEvent(Object event) {
        if (SpringUtils.applicationContext == null) {
            throw new RuntimeException("SpringUtils.applicationContext is not initialized yet, please check your code");
        }
        applicationContext.publishEvent(event);
    }
}
