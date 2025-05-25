package com.zqqiliyc.dto;

import io.mybatis.mapper.example.Example;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-05-25
 */
@Getter
@Setter
public abstract class AbstractQueryDto<T> implements QueryDto<T> {

    private int pageNo = 1;

    private int pageSize = 15;

    @Override
    public Example<T> toExample() {
        return new Example<>();
    }
}
