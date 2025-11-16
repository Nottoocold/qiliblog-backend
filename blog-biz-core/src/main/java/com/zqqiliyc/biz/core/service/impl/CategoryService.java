package com.zqqiliyc.biz.core.service.impl;

import cn.hutool.core.lang.Assert;
import com.zqqiliyc.biz.core.dto.CreateDTO;
import com.zqqiliyc.biz.core.dto.UpdateDTO;
import com.zqqiliyc.biz.core.entity.Category;
import com.zqqiliyc.biz.core.repository.mapper.CategoryMapper;
import com.zqqiliyc.biz.core.service.ICategoryService;
import com.zqqiliyc.biz.core.service.base.AbstractBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

/**
 * @author qili
 * @date 2025-06-01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CategoryService extends AbstractBaseService<Category, Long, CategoryMapper> implements ICategoryService {

    @Override
    public Category create(CreateDTO<Category> dto) {
        Category entity = dto.toEntity();
        validateBeforeCreateWithDB(Map.of(Category::getName, entity.getName()), "分类名称已存在");
        validateBeforeCreateWithDB(Map.of(Category::getSlug, entity.getSlug()), "分类 URL 友好标识符已存在");
        Assert.isTrue(baseMapper.insert(entity) == 1, "insert failed");
        return entity;
    }

    @Override
    public Category update(UpdateDTO<Category> dto) {
        Category entity = findById(dto.getId());
        dto.fillEntity(entity);
        Long[] excludeIds = {entity.getId()};
        validateBeforeUpdateWithDB(Map.of(Category::getName, entity.getName()), "分类名称已存在", excludeIds);
        validateBeforeUpdateWithDB(Map.of(Category::getSlug, entity.getSlug()), "分类 URL 友好标识符已存在", excludeIds);
        Assert.isTrue(baseMapper.updateByPrimaryKey(entity) == 1, "update failed");
        return entity;
    }

    @Override
    public void updateCategoryPostCount(Long categoryId, int delta) {
        Optional<Category> categoryOpt = baseMapper.selectByPrimaryKey(categoryId);
        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            category.setPostCount(Math.max(0, category.getPostCount() + delta));
            baseMapper.updateByPrimaryKey(category);
        }
    }
}
