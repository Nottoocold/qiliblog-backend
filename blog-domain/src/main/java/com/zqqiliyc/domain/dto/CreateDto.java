package com.zqqiliyc.domain.dto;

import com.zqqiliyc.domain.entity.BaseEntity;

/**
 * @author qili
 * @date 2025-06-03
 */
public interface CreateDto<T extends BaseEntity> {

    T toEntity();
}
