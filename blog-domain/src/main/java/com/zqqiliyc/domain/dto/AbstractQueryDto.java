package com.zqqiliyc.domain.dto;

import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.domain.entity.Entity;
import io.mybatis.mapper.example.Example;
import io.mybatis.mapper.fn.Fn;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @author qili
 * @date 2025-05-25
 */
@Getter
@Setter
public abstract class AbstractQueryDto<T extends Entity> implements QueryDto<T> {
    private Integer pageNum;

    private Integer pageSize;

    private String orderBy;

    @Override
    public void addConditionToExample(Example<T> example) {
        if (StrUtil.isNotBlank(orderBy)) {
            List<String> strings = StrUtil.splitTrim(orderBy, " ");
            Fn<T, Object> field = Fn.field(getEntityClass(), strings.get(0));
            if (strings.size() < 2 || "asc".equalsIgnoreCase(strings.get(1))) {
                example.orderByAsc(field);
            } else {
                example.orderByDesc(field);
            }
        }
        fillExample(example);
    }

    @Override
    public boolean isPageRequest() {
        return pageNum != null && pageSize != null;
    }

    @SuppressWarnings("unchecked")
    protected Class<T> getEntityClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected abstract void fillExample(Example<T> example);
}
