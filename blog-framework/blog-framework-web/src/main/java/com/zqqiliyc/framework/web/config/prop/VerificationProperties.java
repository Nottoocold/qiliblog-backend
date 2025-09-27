package com.zqqiliyc.framework.web.config.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author qili
 * @date 2025-07-17
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "qiliblog.verification")
public class VerificationProperties {
    private boolean enabled;
    private String username;
    private int codeLength;
    private int expirationSeconds;
    private String templatePath;
}
