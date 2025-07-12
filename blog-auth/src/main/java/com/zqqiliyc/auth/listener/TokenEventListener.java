package com.zqqiliyc.auth.listener;

import cn.hutool.core.bean.BeanUtil;
import com.zqqiliyc.auth.dto.SysTokenCreateDto;
import com.zqqiliyc.common.token.TokenEvent;
import com.zqqiliyc.service.ISysTokenService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author qili
 * @date 2025-07-12
 */
@Component
public class TokenEventListener {
    private final ISysTokenService tokenService;

    public TokenEventListener(ISysTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @EventListener
    public void onApplicationEvent(TokenEvent event) {
        if (event.getEventType() == TokenEvent.EventType.GENERATE) {
            SysTokenCreateDto createDto = new SysTokenCreateDto();
            BeanUtil.copyProperties(event.getToken(), createDto);
            tokenService.create(createDto);
        }
    }
}
