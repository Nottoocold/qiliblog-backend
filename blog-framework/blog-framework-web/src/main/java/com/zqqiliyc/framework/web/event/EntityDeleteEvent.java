package com.zqqiliyc.framework.web.event;

import lombok.Getter;
import org.springframework.context.PayloadApplicationEvent;

import java.io.Serializable;

/**
 * @author qili
 * @date 2025-10-02
 */
@Getter
public class EntityDeleteEvent<T extends Serializable> extends PayloadApplicationEvent<T> {

    public EntityDeleteEvent(Object source, T entity) {
        super(source, entity);
    }

    public T getEntity() {
        return getPayload();
    }
}
