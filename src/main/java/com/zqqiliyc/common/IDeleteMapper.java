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

    /**
     * 根据Example删除数据, 实际的物理删除，用来给那些继承了{@link io.mybatis.mapper.logical.LogicalMapper}的Mapper接口使用
     *
     * @param example 查询条件
     * @return 受影响的行数
     */
    @Lang(Caching.class)
    @DeleteProvider(type = ExampleProvider.class, method = "deleteByExample")
    int deleteHard(Example<T> example);
}
