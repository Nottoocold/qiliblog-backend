package com.zqqiliyc.module.biz.dto;

import cn.hutool.core.bean.BeanUtil;
import com.zqqiliyc.module.biz.entity.BaseEntity;

import java.io.Serializable;

/**
 * @author qili
 * @date 2025-06-03
 */
public interface UpdateDTO<T extends BaseEntity> {

    <I extends Serializable> I getId();

    void fillEntity(T entity);

    static <M> M fromEntity(BaseEntity entity, Class<M> target) {
        return BeanUtil.copyProperties(entity, target);
    }
}
