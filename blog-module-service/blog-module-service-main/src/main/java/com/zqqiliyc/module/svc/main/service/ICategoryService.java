package com.zqqiliyc.module.svc.main.service;

import com.zqqiliyc.framework.web.service.IBaseService;
import com.zqqiliyc.module.svc.main.domain.entity.Category;

/**
 * @author qili
 * @date 2025-06-01
 */
public interface ICategoryService extends IBaseService<Category, Long> {

    /**
     * 更新分类文章计数
     *
     * @param categoryId 分类ID
     * @param delta      变化量（正数增加，负数减少）
     */
    void updateCategoryPostCount(Long categoryId, int delta);
}
