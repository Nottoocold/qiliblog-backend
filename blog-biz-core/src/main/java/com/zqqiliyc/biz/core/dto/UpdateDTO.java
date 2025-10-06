package com.zqqiliyc.biz.core.dto;

import com.zqqiliyc.biz.core.entity.BaseEntity;

import java.io.Serializable;

/**
 * @author qili
 * @date 2025-06-03
 */
public interface UpdateDTO<T extends BaseEntity> {

    <I extends Serializable> I getId();

    void fillEntity(T entity);

    UpdateDTO<T> fromEntity(T entity);
}
