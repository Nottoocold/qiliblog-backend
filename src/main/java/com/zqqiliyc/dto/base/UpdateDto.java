package com.zqqiliyc.dto.base;

import com.zqqiliyc.domain.entity.BaseEntity;

import java.io.Serializable;

/**
 * @author qili
 * @date 2025-06-03
 */
public interface UpdateDto<T extends BaseEntity> {

    <I extends Serializable> I getId();

    void fillEntity(T entity);

    UpdateDto<T> fromEntity(T entity);
}
