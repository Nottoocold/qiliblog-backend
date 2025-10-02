package com.zqqiliyc.service.impl;

import com.zqqiliyc.domain.dto.CreateDto;
import com.zqqiliyc.domain.dto.UpdateDto;
import com.zqqiliyc.domain.entity.Category;
import com.zqqiliyc.repository.mapper.CategoryMapper;
import com.zqqiliyc.service.ICategoryService;
import com.zqqiliyc.service.base.AbstractBaseService;
import io.mybatis.provider.util.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author qili
 * @date 2025-06-01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CategoryService extends AbstractBaseService<Category, Long, CategoryMapper> implements ICategoryService {

    @Override
    public Category create(CreateDto<Category> dto) {
        validateConstraintAndThrow(dto);
        Category entity = dto.toEntity();
        validateBeforeCreateWithDB(Map.of(Category::getName, entity.getName()), "分类名称已存在");
        validateBeforeCreateWithDB(Map.of(Category::getSlug, entity.getSlug()), "分类 URL 友好标识符已存在");
        Assert.isTrue(baseMapper.insert(entity) == 1, "insert failed");
        return entity;
    }

    @Override
    public Category update(UpdateDto<Category> dto) {
        validateConstraintAndThrow(dto);
        Category entity = findById(dto.getId());
        dto.fillEntity(entity);
        return update(entity);
    }

    @Override
    public Category update(Category entity) {
        Long[] excludeIds = {entity.getId()};
        validateBeforeUpdateWithDB(Map.of(Category::getName, entity.getName()), "分类名称已存在", excludeIds);
        validateBeforeUpdateWithDB(Map.of(Category::getSlug, entity.getSlug()), "分类 URL 友好标识符已存在", excludeIds);
        return super.update(entity);
    }
}
