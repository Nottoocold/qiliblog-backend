package com.zqqiliyc.common;

import io.mybatis.mapper.example.Example;
import io.mybatis.mapper.example.ExampleProvider;
import io.mybatis.provider.Caching;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Lang;

/**
 * @author qili
 * @date 2025-04-06
 */
public interface IDeleteMapper<T> {

    @Lang(Caching.class)
    @DeleteProvider(type = ExampleProvider.class, method = "deleteByExample")
    int deleteHard(Example<T> example);
}
