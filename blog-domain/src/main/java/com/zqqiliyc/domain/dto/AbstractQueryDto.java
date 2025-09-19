package com.zqqiliyc.domain.dto;

import com.zqqiliyc.domain.entity.Entity;
import com.zqqiliyc.framework.web.bean.SortEntry;
import io.mybatis.mapper.example.Example;
import io.mybatis.mapper.fn.Fn;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.ParameterizedType;
import java.util.Set;

/**
 * @author qili
 * @date 2025-05-25
 */
@Getter
@Setter
public abstract class AbstractQueryDto<T extends Entity> implements QueryDto<T> {
    /**
     * 分页页码-从1开始
     */
    private Integer pageNum;
    /**
     * 分页大小
     */
    private Integer pageSize;
    /**
     * 排序字段
     */
    private Set<SortEntry> sortList;

    @Override
    public void addConditionToExample(Example<T> example) {
        if (null != sortList && !sortList.isEmpty()) {
            Class<T> entityClass = getEntityClass();
            for (SortEntry entry : sortList) {
                Fn<T, Object> fn = Fn.field(entityClass, entry.getName());
                example.orderBy(fn, entry.isAsc() ? Example.Order.ASC : Example.Order.DESC);
            }
        }
        fillExample(example);
    }

    @Override
    public boolean isPageRequest() {
        return (null != pageNum && pageNum > 0) && (null != pageSize && pageSize > 0);
    }

    @SuppressWarnings("unchecked")
    protected Class<T> getEntityClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected abstract void fillExample(Example<T> example);
}
