package com.zqqiliyc.framework.web.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

/**
 * @author qili
 * @date 2025-10-02
 */
@Getter
public class EntityDeleteEvent<T extends Serializable> extends ApplicationEvent {
    /**
     * 删除的实体
     */
    private final T entity;

    public EntityDeleteEvent(Object source, T entity) {
        super(source);
        this.entity = entity;
    }
}
