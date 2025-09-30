package com.zqqiliyc.framework.web.bean;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * @author qili
 * @date 2025-07-18
 */
@Getter
@Setter
public class PageResult<T> {
    /**
     * 总记录数
     */
    private int total;
    /**
     * 当前页码
     */
    private int pageNum;
    /**
     * 每页记录数
     */
    private int pageSize;
    /**
     * 总页数
     */
    private int pages;
    /**
     * 是否有上一页
     */
    private boolean hasPre;
    /**
     * 是否有下一页
     */
    private boolean hasNext;
    /**
     * 数据集
     */
    private List<T> list;

    public PageResult() {
        this.list = Collections.emptyList();
    }

    public static <T> PageResult<T> of(PageInfo<T> pageInfo) {
        PageResult<T> result = new PageResult<>();
        result.setTotal(Long.valueOf(pageInfo.getTotal()).intValue());
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setPages(pageInfo.getPages());
        result.setHasPre(pageInfo.isHasPreviousPage());
        result.setHasNext(pageInfo.isHasNextPage());
        result.setList(pageInfo.getList());
        return result;
    }

    public static <T> PageResult<T> emptyPageResult() {
        return new PageResult<>();
    }

    public <U> PageResult<U> map(Function<List<T>, List<U>> mapper) {
        PageResult<U> result = new PageResult<>();
        BeanUtil.copyProperties(this, result, "list");
        result.setList(mapper.apply(this.list));
        return result;
    }
}
