package com.zqqiliyc.common.config.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author qili
 * @date 2025-07-17
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "qiliblog.security")
public class SecurityProperties {
    private List<String> allowedUrls;
}
