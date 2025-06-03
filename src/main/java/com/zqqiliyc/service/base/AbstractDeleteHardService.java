package com.zqqiliyc.service.base;

import com.zqqiliyc.domain.entity.BaseEntityWithDel;
import com.zqqiliyc.mapper.base.IDeleteMapper;
import io.mybatis.mapper.BaseMapper;
import io.mybatis.mapper.example.Example;
import io.mybatis.mapper.fn.Fn;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

/**
 * @author qili
 * @date 2025-05-25
 */
public abstract class AbstractDeleteHardService<T extends BaseEntityWithDel, I extends Serializable, M extends BaseMapper<T, I>>
        extends AbstractExtendService<T, I, M> implements IBaseDeleteHardService<T, I> {

    @Override
    public int deleteHardById(I id) {
        return deleteHardByFieldList(Fn.field(baseMapper.entityClass(), "id"), Collections.singletonList(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F> int deleteHardByFieldList(Fn<T, F> field, Collection<F> fieldValueList) {
        if (!IDeleteMapper.class.isAssignableFrom(baseMapper.getClass())) {
            throw new RuntimeException("mapper interface must be IDeleteMapper");
        }
        IDeleteMapper<T> deleteMapper = (IDeleteMapper<T>) baseMapper;
        Example<T> example = example();
        example.createCriteria().andIn((Fn<T, Object>) field, fieldValueList);
        return deleteMapper.deleteHard(example);
    }
}
