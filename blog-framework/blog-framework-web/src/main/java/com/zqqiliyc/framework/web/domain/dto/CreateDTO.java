package com.zqqiliyc.framework.web.domain.dto;

import com.zqqiliyc.framework.web.domain.entity.BaseEntity;

/**
 * @author qili
 * @date 2025-06-03
 */
public interface CreateDTO<T extends BaseEntity> {

    T toEntity();
}
