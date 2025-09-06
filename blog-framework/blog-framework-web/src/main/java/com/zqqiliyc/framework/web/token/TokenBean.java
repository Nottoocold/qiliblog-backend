package com.zqqiliyc.framework.web.token;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author qili
 * @date 2025-07-13
 */
@Getter
@Setter
public class TokenBean {
    private String accessToken;

    private String refreshToken;

    private String tokenStyle;

    private Long userId;

    private LocalDateTime issuedAt;

    private LocalDateTime expiredAt;

    private LocalDateTime refreshExpiredAt;

    private String additionalInfo;

    private int revoked;

    private LocalDateTime revokedAt;
}
