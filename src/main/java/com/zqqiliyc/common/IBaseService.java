package com.zqqiliyc.common;

import io.mybatis.mapper.example.Example;
import io.mybatis.mapper.fn.Fn;

import java.util.Collection;
import java.util.List;

/**
 * @author qili
 * @date 2025-04-05
 */
public interface IBaseService<T, I> {

    T findById(I id);

    T findOne(Example<T> example);

    List<T> findList(Example<T> example);

    long count(Example<T> example);

    <F> List<T> findByFieldList(Fn<T, F> field, Collection<F> fieldValueList);

    T save(T entity);

    T update(T entity);

    T updateSelective(T entity);

    T saveOrUpdate(T entity);

    int deleteById(I id);

    <F> int deleteByFieldList(Fn<T, F> field, Collection<F> fieldValueList);
}
