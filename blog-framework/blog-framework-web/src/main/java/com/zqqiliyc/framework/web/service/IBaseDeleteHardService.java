package com.zqqiliyc.framework.web.service;

import com.zqqiliyc.framework.web.domain.entity.BaseEntityWithDel;
import io.mybatis.mapper.fn.Fn;

import java.util.Collection;

/**
 * @author qili
 * @date 2025-05-25
 */
public interface IBaseDeleteHardService<T extends BaseEntityWithDel, I> {

    int deleteHardById(I id);

    <F> int deleteHardByFieldList(Fn<T, F> field, Collection<F> fieldValueList);
}
