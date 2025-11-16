package com.zqqiliyc.framework.web.event;

import lombok.Getter;
import org.springframework.context.PayloadApplicationEvent;

import java.io.Serializable;

/**
 * @author qili
 * @date 2025-11-16
 */
@Getter
public class EntityUpdateEvent<T extends Serializable> extends PayloadApplicationEvent<T> {
    private final T oldEntity;

    public EntityUpdateEvent(Object source, T entity, T oldEntity) {
        super(source, entity);
        this.oldEntity = oldEntity;
    }

    public T getEntity() {
        return getPayload();
    }
}
