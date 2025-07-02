package com.zqqiliyc.common.config;

import com.zqqiliyc.common.config.config.JwtProperties;
import com.zqqiliyc.common.security.BCryptPasswordEncoder;
import com.zqqiliyc.common.security.PasswordEncoder;
import com.zqqiliyc.common.utils.JwtUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

/**
 * @author zqqiliyc
 * @since 2025-07-02
 */
@Configuration(proxyBeanMethods = false)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class SecurityConfig {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public JwtUtils jwtUtils(JwtProperties jwtProperties) {
        return new JwtUtils(jwtProperties);
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
