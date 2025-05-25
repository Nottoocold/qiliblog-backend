package com.zqqiliyc.dto;

import io.mybatis.mapper.example.Example;

/**
 * @author qili
 * @date 2025-05-25
 */
public interface QueryDto<T> {

    Example<T> toExample();
}
