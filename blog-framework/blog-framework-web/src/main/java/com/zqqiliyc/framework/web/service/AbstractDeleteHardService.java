package com.zqqiliyc.framework.web.service;

import com.zqqiliyc.framework.web.domain.entity.BaseEntityWithDel;
import com.zqqiliyc.framework.web.mapper.IDeleteMapper;
import io.mybatis.mapper.BaseMapper;
import io.mybatis.mapper.example.Example;
import io.mybatis.mapper.fn.Fn;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author qili
 * @date 2025-05-25
 */
@Transactional(rollbackFor = Exception.class)
public abstract class AbstractDeleteHardService<T extends BaseEntityWithDel, I extends Serializable, M extends BaseMapper<T, I>>
        extends AbstractBaseService<T, I, M> implements IBaseDeleteHardService<T, I> {

    @Override
    public int deleteHardById(I id) {
        return baseMapper.deleteByPrimaryKey(id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F> int deleteHardByFieldList(Fn<T, F> field, Collection<F> fieldValueList) {
        if (!IDeleteMapper.class.isAssignableFrom(baseMapper.getClass())) {
            throw new RuntimeException("mapper interface must be IDeleteMapper");
        }
        IDeleteMapper<T> deleteMapper = (IDeleteMapper<T>) baseMapper;
        Example<T> example = new Example<>();
        example.createCriteria().andIn((Fn<T, Object>) field, fieldValueList);
        return deleteMapper.deleteHard(example);
    }
}
