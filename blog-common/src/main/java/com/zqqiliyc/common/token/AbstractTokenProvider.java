package com.zqqiliyc.common.token;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * @author qili
 * @date 2025-07-12
 */
public abstract class AbstractTokenProvider implements TokenProvider, InitializingBean {
    private ApplicationEventPublisher eventPublisher;
    private Environment environment;

    @Autowired
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    protected void publishTokenEvent(TokenEvent event) {
        if (eventPublisher != null) {
            eventPublisher.publishEvent(event);
        }
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(eventPublisher, "ApplicationEventPublisher cannot be null");
        Assert.notNull(environment, "Environment cannot be null");
        init(environment);
    }

    protected void init(Environment environment) {

    }

    @Override
    public Token generateToken(Long userId, Map<String, Object> payload) {
        Token token = doGenerateToken(userId, payload);
        Assert.notNull(token, "Token object cannot be null");
        Assert.isTrue(StrUtil.isNotBlank(token.getAccessToken()), "Token's access token cannot be null");
        publishTokenEvent(new TokenEvent(token, TokenEvent.EventType.GENERATE));
        return token;
    }

    @Override
    public Token refreshToken(String refreshToken) {
        throw new UnsupportedOperationException("Refresh token is not supported");
    }

    @Override
    public void revokeToken(String accessToken) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRevokedReason(getRevokedReason());
        publishTokenEvent(new TokenEvent(token, TokenEvent.EventType.REVOKE));
    }

    protected abstract Token doGenerateToken(Long userId, Map<String, Object> payload);

    protected String getRevokedReason() {
        return "Token revoked";
    }
}
