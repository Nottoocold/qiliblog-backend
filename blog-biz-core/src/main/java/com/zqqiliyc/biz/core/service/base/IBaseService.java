package com.zqqiliyc.biz.core.service.base;

import com.zqqiliyc.biz.core.dto.CreateDTO;
import com.zqqiliyc.biz.core.dto.QueryDTO;
import com.zqqiliyc.biz.core.dto.UpdateDTO;
import com.zqqiliyc.biz.core.entity.BaseEntity;
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

    PageResult<T> findPageInfo(QueryDTO<T> queryDto);

    default List<T> findList(QueryDTO<T> queryDto) {
        return findList(queryDto.toExample());
    }

    List<T> findList(Example<T> example);

    long count(Example<T> example);

    <F> List<T> findByFieldList(Fn<T, F> field, Collection<F> fieldValueList);

    T create(CreateDTO<T> dto);

    T update(UpdateDTO<T> dto);

    void deleteById(I id);

    <F> int deleteByFieldList(Fn<T, F> field, Collection<F> fieldValueList);
}
