package com.zqqiliyc.framework.web.event;

import lombok.Getter;
import org.springframework.context.PayloadApplicationEvent;

import java.io.Serializable;

/**
 * @author qili
 * @date 2025-11-16
 */
@Getter
public class EntityCreateEvent<T extends Serializable> extends PayloadApplicationEvent<T> {

    public EntityCreateEvent(Object source, T entity) {
        super(source, entity);
    }

    public T getEntity() {
        return getPayload();
    }
}
