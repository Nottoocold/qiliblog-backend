package com.zqqiliyc.biz.core.dto;

import com.zqqiliyc.biz.core.entity.Entity;
import io.mybatis.mapper.example.Example;

/**
 * @author qili
 * @date 2025-05-25
 */
public interface QueryDTO<T extends Entity> {

    default Example<T> toExample() {
        Example<T> example = new Example<>();
        addConditionToExample(example);
        return example;
    }

    boolean isPageRequest();

    Integer getCurrent();

    Integer getPageSize();

    void addConditionToExample(Example<T> example);
}
