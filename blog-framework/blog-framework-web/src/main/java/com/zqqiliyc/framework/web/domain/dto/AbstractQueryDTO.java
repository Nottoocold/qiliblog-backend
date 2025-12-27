package com.zqqiliyc.framework.web.domain.dto;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import com.zqqiliyc.framework.web.domain.entity.Entity;
import io.mybatis.mapper.example.Example;
import io.mybatis.mapper.fn.Fn;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author qili
 * @date 2025-05-25
 */
@Getter
@Setter
public abstract class AbstractQueryDTO<T extends Entity> implements QueryDTO<T> {
    /**
     * 分页页码-从1开始
     */
    private Integer current;
    /**
     * 分页大小
     */
    private Integer pageSize;
    /**
     * 排序字段:形如 name desc,age,time asc
     */
    private String sortBy;

    @Override
    public void addConditionToExample(Example<T> example) {
        if (StrUtil.isNotBlank(sortBy)) {
            Class<T> entityClass = getEntityClass();
            List<String> sortEntry = StrUtil.splitTrim(sortBy, ",");
            for (String entry : sortEntry) {
                String[] split = entry.split("\\s+");
                String name = split[0];
                String order = split.length > 1 ? split[1].toLowerCase() : "asc";
                example.orderBy(Fn.field(entityClass, name),
                        "asc".equals(order) ? Example.Order.ASC : Example.Order.DESC);
            }
        }
        fillExample(example);
    }

    @Override
    public boolean isPageRequest() {
        return (null != current && current > 0) && (null != pageSize && pageSize > 0);
    }

    @SuppressWarnings("unchecked")
    protected Class<T> getEntityClass() {
        return (Class<T>) TypeUtil.getClass(TypeUtil.getTypeArgument(getClass(), 0));
    }

    protected abstract void fillExample(Example<T> example);
}
