package com.zqqiliyc.common.token;

import cn.hutool.core.lang.Assert;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Map;

/**
 * @author qili
 * @date 2025-07-13
 */
public abstract class AbstractTokenProvider implements TokenProvider {
    private ApplicationEventPublisher eventPublisher;

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(eventPublisher, "ApplicationEventPublisher must not be null");
        init();
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    protected void init() {
        // let subclasses to do initialization
    }

    protected void publishEvent(TokenEvent event) {
        eventPublisher.publishEvent(event);
    }

    @Override
    public TokenBean generateToken(Long userId, Map<String, Object> claims) {
        TokenBean tokenBean = doGenerateToken(userId, claims);
        publishEvent(new TokenEvent(tokenBean, TokenEventType.GENERATE));
        return tokenBean;
    }

    @Override
    public TokenBean refreshToken(String refreshToken) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void revokeToken(String accessToken) {
        TokenBean tokenBean = new TokenBean();
        tokenBean.setAccessToken(accessToken);
        publishEvent(new TokenEvent(tokenBean, TokenEventType.REVOKE));
    }

    protected abstract TokenBean doGenerateToken(Long userId, Map<String, Object> claims);
}
