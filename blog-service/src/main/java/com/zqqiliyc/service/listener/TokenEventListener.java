package com.zqqiliyc.service.listener;

import cn.hutool.core.bean.BeanUtil;
import com.zqqiliyc.common.token.TokenBean;
import com.zqqiliyc.common.token.TokenEvent;
import com.zqqiliyc.common.token.TokenEventType;
import com.zqqiliyc.domain.dto.token.SysTokenCreateDto;
import com.zqqiliyc.service.ISysTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author qili
 * @date 2025-07-13
 */
@Slf4j
@Component
public class TokenEventListener {
    private final ISysTokenService tokenService;

    public TokenEventListener(ISysTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @EventListener
    public void onEvent(TokenEvent event) {
        if (event.eventType() == TokenEventType.GENERATE) {
            createToken(event.token());
        } else if (event.eventType() == TokenEventType.REFRESH) {
            refreshToken(event.token());
        } else if (event.eventType() == TokenEventType.REVOKE) {
            revokeToken(event.token());
        }
    }

    private void createToken(TokenBean tokenBean) {
        if (log.isDebugEnabled()) {
            log.debug("store token to db start.");
        }
        SysTokenCreateDto dto = new SysTokenCreateDto();
        BeanUtil.copyProperties(tokenBean, dto);
        tokenService.create(dto);
        if (log.isDebugEnabled()) {
            log.debug("store token to db success.");
        }
    }

    private void refreshToken(TokenBean tokenBean) {}

    private void revokeToken(TokenBean tokenBean) {}
}
