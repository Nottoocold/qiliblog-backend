package com.zqqiliyc.service.article;

import com.zqqiliyc.BaseTestApp;
import com.zqqiliyc.framework.common.utils.PinyinUtils;
import com.zqqiliyc.module.svc.main.domain.entity.Category;
import com.zqqiliyc.module.svc.main.domain.entity.Tag;
import com.zqqiliyc.module.svc.main.mapper.CategoryMapper;
import com.zqqiliyc.module.svc.main.mapper.TagMapper;
import io.mybatis.mapper.example.Example;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;

import java.util.List;

/**
 * @author qili
 * @date 2026-01-11
 */
public class BaseBlogService extends BaseTestApp {
    private static final List<String> categoryNameList = List.of("测试分类1", "测试分类2");
    private static final List<String> tagNameList = List.of("测试标签1", "测试标签2", "测试标签3");

    @BeforeEach
    @Order(Integer.MIN_VALUE + 1)
    public void init() {
        transactionTemplate.executeWithoutResult(transaction -> {
            CategoryMapper categoryMapper = sqlSessionTemplate.getMapper(CategoryMapper.class);
            long cateSize = categoryMapper.countByExample(new Example<>());
            if (cateSize < categoryNameList.size()) {
                for (int i = 0; i < categoryNameList.size() - cateSize; i++) {
                    Category category = new Category();
                    category.setName(categoryNameList.get(i));
                    category.setSlug(PinyinUtils.toPinyin(categoryNameList.get(i)));
                    categoryMapper.insert(category);
                }
            }
            TagMapper tagMapper = sqlSessionTemplate.getMapper(TagMapper.class);
            long tagSize = tagMapper.countByExample(new Example<>());
            if (tagSize < tagNameList.size()) {
                for (int i = 0; i < tagNameList.size() - tagSize; i++) {
                    Tag tag = new Tag();
                    tag.setName(tagNameList.get(i));
                    tag.setSlug(PinyinUtils.toPinyin(tagNameList.get(i)));
                    tagMapper.insert(tag);
                }
            }
            transaction.flush();
        });
    }

    protected List<Category> getCategoryList() {
        CategoryMapper categoryMapper = sqlSessionTemplate.getMapper(CategoryMapper.class);
        return categoryMapper.selectByExample(new Example<>());
    }

    protected List<Tag> getTagList() {
        TagMapper tagMapper = sqlSessionTemplate.getMapper(TagMapper.class);
        return tagMapper.selectByExample(new Example<>());
    }
}
