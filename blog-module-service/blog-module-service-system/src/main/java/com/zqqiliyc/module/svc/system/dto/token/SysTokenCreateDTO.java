package com.zqqiliyc.module.svc.system.dto.token;

import cn.hutool.core.bean.BeanUtil;
import com.zqqiliyc.framework.web.domain.dto.CreateDTO;
import com.zqqiliyc.framework.web.enums.TokenStyle;
import com.zqqiliyc.module.svc.system.entity.SysToken;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author qili
 * @date 2025-07-13
 */
@Getter
@Setter
public class SysTokenCreateDTO implements CreateDTO<SysToken> {
    private String accessToken;

    private String refreshToken;

    private TokenStyle tokenStyle;

    private Long userId;

    private LocalDateTime issuedAt;

    private LocalDateTime expiredAt;

    private LocalDateTime refreshExpiredAt;

    private int revoked;

    private LocalDateTime revokedAt;

    private String ipAddress;

    private Map<String, Object> additionalInfo;

    @Override
    public SysToken toEntity() {
        SysToken sysToken = new SysToken();
        BeanUtil.copyProperties(this, sysToken);
        return sysToken;
    }
}
