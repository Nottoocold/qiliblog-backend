package com.zqqiliyc.dto.base;

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

    void addConditionToExample(Example<T> example);
}
