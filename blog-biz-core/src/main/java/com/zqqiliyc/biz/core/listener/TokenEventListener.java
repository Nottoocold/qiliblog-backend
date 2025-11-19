package com.zqqiliyc.biz.core.listener;

import cn.hutool.core.bean.BeanUtil;
import com.zqqiliyc.biz.core.dto.token.SysTokenCreateDTO;
import com.zqqiliyc.biz.core.service.ISysTokenService;
import com.zqqiliyc.framework.web.token.TokenBean;
import com.zqqiliyc.framework.web.token.TokenEvent;
import com.zqqiliyc.framework.web.token.TokenEventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
        } else if (event.eventType() == TokenEventType.REVOKE) {
            revokeToken(event.token());
        }
    }

    private void createToken(TokenBean tokenBean) {
        if (log.isDebugEnabled()) {
            log.debug("store token to db start.");
        }
        SysTokenCreateDTO dto = new SysTokenCreateDTO();
        BeanUtil.copyProperties(tokenBean, dto);
        ServletRequestAttributes currented = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        String remoteAddr = currented.getRequest().getRemoteAddr();
        dto.setIpAddress(remoteAddr);
        tokenService.create(dto);
        if (log.isDebugEnabled()) {
            log.debug("store token to db success.");
        }
    }

    private void revokeToken(TokenBean tokenBean) {
        if (log.isDebugEnabled()) {
            log.debug("revoke token from db start.");
        }
        tokenService.revoke(tokenBean.getAccessToken());
        if (log.isDebugEnabled()) {
            log.debug("revoke token from db success.");
        }
    }
}
