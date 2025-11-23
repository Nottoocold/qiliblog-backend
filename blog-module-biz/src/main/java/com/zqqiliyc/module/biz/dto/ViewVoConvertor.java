package com.zqqiliyc.module.biz.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.TypeUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author qili
 * @date 2025-09-30
 */
public interface ViewVoConvertor<S, T> {

    /**
     * 转换为视图对象
     *
     * @param source 源对象
     * @return 目标对象
     */
    default T toViewVo(S source) {
        if (source == null) {
            return null;
        }
        List<S> sourceList = new ArrayList<>(1);
        sourceList.add(source);
        List<T> result = this.toViewVoList(sourceList);
        return CollUtil.isEmpty(result) ? null : result.get(0);
    }

    /**
     * 批量转换为视图对象
     *
     * @param sourceList 源对象列表
     * @return 目标对象列表
     */
    default List<T> toViewVoList(List<S> sourceList) {
        if (CollUtil.isEmpty(sourceList)) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>(sourceList.size());
        for (S s : sourceList) {
            T t = newInstance();
            BeanUtil.copyProperties(s, t);
            customize(t);
            result.add(t);
        }
        customize(result, sourceList);
        return result;
    }

    /**
     * 创建新的实例
     *
     * @return 新的目标实例
     */
    @SuppressWarnings("unchecked")
    default T newInstance() {
        Type typeArgument = TypeUtil.getTypeArgument(this.getClass(), 1);
        Class<?> aClass = TypeUtil.getClass(typeArgument);
        return (T) ReflectUtil.newInstance(aClass);
    }

    /**
     * 自定义定制目标对象，注意：这是在循环体中运行的，请勿执行耗时操作，如需耗时操作请使用 customize(List, List)
     *
     * @param vo 目标对象
     * @see ViewVoConvertor#customize(List, List)
     */
    default void customize(T vo) {
    }

    /**
     * 自定义定制目标对象列表,注意：这是在循环执行完之后允许的，voList集合已经有值
     *
     * @param targetList     目标对象列表
     * @param sourceList 源对象列表
     */
    default void customize(List<T> targetList, List<S> sourceList) {
    }
}
