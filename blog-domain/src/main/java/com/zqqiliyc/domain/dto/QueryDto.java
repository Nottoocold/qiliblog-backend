package com.zqqiliyc.domain.dto;

import com.zqqiliyc.domain.entity.Entity;
import io.mybatis.mapper.example.Example;

/**
 * @author qili
 * @date 2025-05-25
 */
public interface QueryDto<T extends Entity> {

    default Example<T> toExample() {
        Example<T> example = new Example<>();
        addConditionToExample(example);
        return example;
    }

    boolean isPageRequest();

    Integer getPageNum();

    Integer getPageSize();

    void addConditionToExample(Example<T> example);
}
