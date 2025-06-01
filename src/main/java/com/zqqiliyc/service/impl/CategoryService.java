package com.zqqiliyc.service.impl;

import com.zqqiliyc.domain.entity.Category;
import com.zqqiliyc.mapper.CategoryMapper;
import com.zqqiliyc.service.ICategoryService;
import io.mybatis.service.AbstractService;
import org.springframework.stereotype.Service;

/**
 * @author qili
 * @date 2025-06-01
 */
@Service
public class CategoryService extends AbstractService<Category, Long, CategoryMapper> implements ICategoryService {
}
