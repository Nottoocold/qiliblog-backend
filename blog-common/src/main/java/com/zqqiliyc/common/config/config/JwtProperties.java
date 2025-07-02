package com.zqqiliyc.common.config.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Role;
import org.springframework.stereotype.Component;

/**
 * @author qili
 * @date 2025-06-29
 */
@Getter
@Setter
@Component
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@ConfigurationProperties(prefix = "qiliblog.security.jwt")
public class JwtProperties {

    private String secret;

    private long expire = 3600L;
}
