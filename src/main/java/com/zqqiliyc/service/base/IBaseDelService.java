package com.zqqiliyc.service.base;

import io.mybatis.mapper.fn.Fn;

import java.util.Collection;

/**
 * @author qili
 * @date 2025-05-25
 */
public interface IBaseDelService<T, I> {

    int deleteHardById(I id);

    <F> int deleteHardByFieldList(Fn<T, F> field, Collection<F> fieldValueList);
}
