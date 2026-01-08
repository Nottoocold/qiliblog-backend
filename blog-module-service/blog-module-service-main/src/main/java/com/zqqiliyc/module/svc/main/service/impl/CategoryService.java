package com.zqqiliyc.module.svc.main.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import com.zqqiliyc.framework.web.exception.ClientException;
import com.zqqiliyc.framework.web.service.AbstractBaseService;
import com.zqqiliyc.module.svc.main.domain.entity.Category;
import com.zqqiliyc.module.svc.main.mapper.CategoryMapper;
import com.zqqiliyc.module.svc.main.service.ICategoryService;
import io.mybatis.mapper.fn.Fn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author qili
 * @date 2025-06-01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CategoryService extends AbstractBaseService<Category, Long, CategoryMapper> implements ICategoryService {

    @Override
    protected void beforeCreate(Category entity) {
        if (wrapper().eq(Category::getName, entity.getName()).one().isPresent()) {
            throw new ClientException(GlobalErrorDict.PARAM_ERROR, "分类名称已存在");
        }
        if (wrapper().eq(Category::getSlug, entity.getSlug()).one().isPresent()) {
            throw new ClientException(GlobalErrorDict.PARAM_ERROR, "分类 URL 友好标识符已存在");
        }
    }

    @Override
    protected void afterCreate(Category entity) {

    }

    @Override
    protected void beforeUpdate(Category entity) {
        if (wrapper().eq(Category::getName, entity.getName()).ne(Category::getId, entity.getId()).one().isPresent()) {
            throw new ClientException(GlobalErrorDict.PARAM_ERROR, "分类名称已存在");
        }
        if (wrapper().eq(Category::getSlug, entity.getSlug()).ne(Category::getId, entity.getId()).one().isPresent()) {
            throw new ClientException(GlobalErrorDict.PARAM_ERROR, "分类 URL 友好标识符已存在");
        }
    }

    @Override
    protected void afterUpdate(Category entity) {

    }

    @Override
    public void updateCategoryPostCount(Long categoryId, int delta) {
        Fn<Category, Object> field = Fn.field(baseMapper.entityClass(), Category::getPostCount);
        String column = field.toColumn();
        String operator = delta >= 0 ? "+" : "-";
        baseMapper.wrapper()
                .eq(Category::getId, categoryId)
                .set(StrUtil.format("{} = {} {} {}", column, column, operator, Math.abs(delta)))
                .set(Category::getUpdateTime, LocalDateTime.now())
                .update();
    }

    @Override
    protected void beforeDelete(Category category) {
        Assert.isTrue(category.getPostCount() == 0, () -> new ClientException(GlobalErrorDict.PARAM_ERROR, "该分类下有文章，无法删除"));
    }

    @Override
    protected void afterDelete(Category entity) {

    }
}
