package com.zqqiliyc.auth.dto;

import cn.hutool.core.bean.BeanUtil;
import com.zqqiliyc.domain.dto.CreateDto;
import com.zqqiliyc.domain.entity.SysToken;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author qili
 * @date 2025-07-12
 */
@Getter
@Setter
public class SysTokenCreateDto implements CreateDto<SysToken> {
    private String accessToken;
    private String refreshToken;
    private String tokenStyle;
    private Long userId;
    private LocalDateTime issuedAt;
    private LocalDateTime expiredAt;
    private LocalDateTime refreshExpiredAt;
    private int revoked;
    private LocalDateTime revokedAt;
    private String revokedReason;
    private String ipAddress;
    private String additionalInfo;

    @Override
    public SysToken toEntity() {
        return BeanUtil.copyProperties(this, SysToken.class);
    }
}
