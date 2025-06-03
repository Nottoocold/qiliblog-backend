package com.zqqiliyc.service.base;

import com.zqqiliyc.domain.entity.BaseEntity;
import com.zqqiliyc.dto.base.CreateDto;
import com.zqqiliyc.dto.base.UpdateDto;
import io.mybatis.mapper.BaseMapper;
import io.mybatis.provider.util.Assert;
import io.mybatis.service.AbstractService;

import java.io.Serializable;

/**
 * @author qili
 * @date 2025-06-03
 */
public class AbstractExtendService<T extends BaseEntity, I extends Serializable, M extends BaseMapper<T, I>>
    extends AbstractService<T, I, M> implements IBaseService<T, I> {

    @Override
    public T create(CreateDto<T> dto) {
        T entity = dto.toEntity();
        Assert.isTrue(baseMapper.insert(entity) == 1, "insert failed");
        return entity;
    }

    @Override
    public T update(UpdateDto<T> dto) {
        T entity = findById(dto.getId());
        dto.fillEntity(entity);
        Assert.isTrue(baseMapper.updateByPrimaryKey(entity) == 1, "update failed");
        return entity;
    }
}
