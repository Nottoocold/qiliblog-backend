package com.zqqiliyc.module.biz.dto;

import com.zqqiliyc.module.biz.entity.BaseEntity;

/**
 * @author qili
 * @date 2025-06-03
 */
public interface CreateDTO<T extends BaseEntity> {

    T toEntity();
}
