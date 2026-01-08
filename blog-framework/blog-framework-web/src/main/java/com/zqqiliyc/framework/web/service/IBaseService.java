package com.zqqiliyc.framework.web.service;

import com.zqqiliyc.framework.web.bean.PageResult;
import com.zqqiliyc.framework.web.domain.dto.CreateDTO;
import com.zqqiliyc.framework.web.domain.dto.QueryDTO;
import com.zqqiliyc.framework.web.domain.dto.UpdateDTO;
import com.zqqiliyc.framework.web.domain.entity.BaseEntity;
import io.mybatis.mapper.example.Example;
import io.mybatis.mapper.fn.Fn;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author qili
 * @date 2025-04-05
 */
public interface IBaseService<T extends BaseEntity, I extends Serializable> {

    T findById(I id);

    T findOne(Example<T> example);

    PageResult<T> findPageInfo(QueryDTO<T> queryDto);

    default List<T> findList(QueryDTO<T> queryDto) {
        return findList(queryDto.toExample());
    }

    List<T> findList(Example<T> example);

    long count(Example<T> example);

    <F> List<T> findByFieldList(Fn<T, F> field, Collection<F> fieldValueList);

    default T create(CreateDTO<T> dto) {
        return create(dto.toEntity());
    }

    T create(T entity);

    default T update(UpdateDTO<T> dto) {
        T entity = findById(dto.getId());
        dto.fillEntity(entity);
        return update(entity);
    }

    T update(T entity);

    void deleteById(I id);

    <F> int deleteByFieldList(Fn<T, F> field, Collection<F> fieldValueList);
}
