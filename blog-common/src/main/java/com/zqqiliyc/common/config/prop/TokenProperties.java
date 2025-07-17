package com.zqqiliyc.common.config.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author qili
 * @date 2025-07-17
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "qiliblog.token")
public class TokenProperties {
    private String style;
    private String secret;
    private long expire;
    private long refreshExpire;
}
