package com.zqqiliyc.biz.core.dto;

import com.zqqiliyc.biz.core.entity.BaseEntity;

/**
 * @author qili
 * @date 2025-06-03
 */
public interface CreateDTO<T extends BaseEntity> {

    T toEntity();
}
