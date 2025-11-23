package com.zqqiliyc.module.biz.service;

import cn.hutool.core.util.RandomUtil;
import com.zqqiliyc.BaseTestApp;
import com.zqqiliyc.framework.common.utils.PinyinUtils;
import com.zqqiliyc.framework.web.spring.SpringUtils;
import com.zqqiliyc.module.biz.dto.article.ArticleDraftCreateDTO;
import com.zqqiliyc.module.biz.dto.cate.CategoryCreateDTO;
import com.zqqiliyc.module.biz.dto.tag.TagCreateDTO;
import com.zqqiliyc.module.biz.entity.Category;
import com.zqqiliyc.module.biz.entity.Tag;
import io.mybatis.mapper.example.Example;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author qili
 * @date 2025-11-23
 */
class IArticleServiceTest extends BaseTestApp {
    @Autowired
    IArticleService articleService;
    static final List<Category> categories = new ArrayList<>();
    static final List<Tag> tags = new ArrayList<>();

    @Test
    @Order(-99)
    public void setup() {
        int categoryCount = 10;
        int tagCount = 10;
        ICategoryService categoryService = SpringUtils.getBean(ICategoryService.class);
        ITagService tagService = SpringUtils.getBean(ITagService.class);
        categories.addAll(categoryService.findList(new Example<>()));
        // 创建几个测试用的分类记录
        if (categories.size() < categoryCount) {
            int rest = categoryCount - categories.size();
            for (int i = 0; i < rest; i++) {
                CategoryCreateDTO categoryCreateDTO = new CategoryCreateDTO();
                categoryCreateDTO.setName(generateString(5, () -> String.valueOf(RandomUtil.randomChinese())));
                categoryCreateDTO.setSlug(PinyinUtils.getFirstLetter(categoryCreateDTO.getName(), false));
                categories.add(categoryService.create(categoryCreateDTO));
            }
        }
        // 创建几个测试用的标签记录
        tags.addAll(tagService.findList(new Example<>()));
        if (tags.size() < tagCount) {
            int rest = tagCount - tags.size();
            for (int i = 0; i < rest; i++) {
                TagCreateDTO tagCreateDTO = new TagCreateDTO();
                tagCreateDTO.setName(generateString(5, () -> String.valueOf(RandomUtil.randomChinese())));
                tagCreateDTO.setSlug(PinyinUtils.getFirstLetter(tagCreateDTO.getName(), false));
                tags.add(tagService.create(tagCreateDTO));
            }
        }
    }

    @Test
    @Order(0)
    void testCreateDraft() {
        ArticleDraftCreateDTO draftCreateDTO = new ArticleDraftCreateDTO();
        draftCreateDTO.setTitle(generateString(10, () -> String.valueOf(RandomUtil.randomChinese())));
        draftCreateDTO.setContent(generateString(100000, () -> String.valueOf(RandomUtil.randomChinese())));
        draftCreateDTO.setSummary(generateString(50, () -> String.valueOf(RandomUtil.randomChinese())));
        draftCreateDTO.setAuthorId(1L);
        draftCreateDTO.setCategoryId(categories.get(RandomUtil.randomInt(categories.size())).getId());
        Set<Tag> tagSet = RandomUtil.randomEleSet(tags, 3);
        draftCreateDTO.setTagIds(tagSet.stream().map(Tag::getId).toList());
        articleService.createDraft(draftCreateDTO);
    }
}