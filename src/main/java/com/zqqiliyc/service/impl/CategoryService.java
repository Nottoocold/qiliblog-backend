package com.zqqiliyc.service.impl;

import com.zqqiliyc.domain.entity.Category;
import com.zqqiliyc.mapper.CategoryMapper;
import com.zqqiliyc.service.ICategoryService;
import com.zqqiliyc.service.base.AbstractBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qili
 * @date 2025-06-01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CategoryService extends AbstractBaseService<Category, Long, CategoryMapper> implements ICategoryService {
}
