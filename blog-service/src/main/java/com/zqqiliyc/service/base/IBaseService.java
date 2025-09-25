package com.zqqiliyc.service.base;

import com.zqqiliyc.domain.dto.CreateDto;
import com.zqqiliyc.domain.dto.QueryDto;
import com.zqqiliyc.domain.dto.UpdateDto;
import com.zqqiliyc.domain.entity.BaseEntity;
import com.zqqiliyc.framework.web.bean.PageResult;
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

    PageResult<T> findPageInfo(QueryDto<T> queryDto);

    List<T> findList(Example<T> example);

    long count(Example<T> example);

    <F> List<T> findByFieldList(Fn<T, F> field, Collection<F> fieldValueList);

    T create(CreateDto<T> dto);

    T update(UpdateDto<T> dto);

    T update(T entity);

    int deleteById(I id);

    <F> int deleteByFieldList(Fn<T, F> field, Collection<F> fieldValueList);
}
