package com.zqqiliyc.biz.core.dto.token;

import cn.hutool.core.bean.BeanUtil;
import com.zqqiliyc.biz.core.dto.CreateDto;
import com.zqqiliyc.biz.core.entity.SysToken;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author qili
 * @date 2025-07-13
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

    private String ipAddress;

    private String additionalInfo;

    @Override
    public SysToken toEntity() {
        SysToken sysToken = new SysToken();
        BeanUtil.copyProperties(this, sysToken);
        return sysToken;
    }
}
