package com.zqqiliyc.common.token;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author qili
 * @date 2025-07-12
 */
@Getter
public class TokenEvent extends ApplicationEvent {
    private final Token token;
    private final EventType eventType;

    public TokenEvent(Token token, EventType eventType) {
        super(TokenEvent.class.getName());
        this.token = token;
        this.eventType = eventType;
    }

    public enum EventType {
        GENERATE, REFRESH, REVOKE
    }
}
