package com.zqqiliyc.biz.core.vo.transfer;

import com.zqqiliyc.biz.core.dto.ViewVoConvertor;
import com.zqqiliyc.biz.core.entity.Article;
import com.zqqiliyc.biz.core.entity.Category;
import com.zqqiliyc.biz.core.entity.SysUser;
import com.zqqiliyc.biz.core.entity.Tag;
import com.zqqiliyc.biz.core.service.ICategoryService;
import com.zqqiliyc.biz.core.service.IRelArticleTagService;
import com.zqqiliyc.biz.core.service.ISysUserService;
import com.zqqiliyc.biz.core.service.ITagService;
import com.zqqiliyc.biz.core.vo.ArticleVo;
import com.zqqiliyc.biz.core.vo.CategoryVo;
import com.zqqiliyc.biz.core.vo.TagVo;
import com.zqqiliyc.framework.web.spring.SpringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author qili
 * @date 2025-09-30
 */
@Component
public class ArticleVoConvertor implements ViewVoConvertor<Article, ArticleVo> {
    private final ICategoryService categoryService;
    private final IRelArticleTagService relArticleTagService;
    private final ITagService tagService;
    private final ISysUserService sysUserService;

    public ArticleVoConvertor(ICategoryService categoryService,
                              IRelArticleTagService relArticleTagService,
                              ITagService tagService,
                              ISysUserService sysUserService) {
        this.categoryService = categoryService;
        this.relArticleTagService = relArticleTagService;
        this.tagService = tagService;
        this.sysUserService = sysUserService;
    }

    @Override
    public void customize(List<ArticleVo> targetList, List<Article> sourceList) {
        CategoryVoConvertor categoryVoConvertor = SpringUtils.getBean(CategoryVoConvertor.class);
        TagVoConvertor tagVoConvertor = SpringUtils.getBean(TagVoConvertor.class);
        Map<Long, CategoryVo> categoryMap = categoryVoConvertor.toViewVoList(categoryService.findByFieldList(Category::getId, sourceList.stream().map(Article::getCategoryId).toList()))
                .stream().collect(Collectors.toMap(CategoryVo::getId, Function.identity()));

        Map<Long, Set<Long>> postTagIdMap = new HashMap<>(sourceList.size());
        sourceList.forEach(article -> {
            postTagIdMap.put(article.getId(), relArticleTagService.findByArticleId(article.getId()));
        });

        Map<Long, SysUser> userMap = sysUserService.findByFieldList(SysUser::getId, sourceList.stream().map(Article::getAuthorId).toList())
                .stream().collect(Collectors.toMap(SysUser::getId, Function.identity()));

        for (ArticleVo vo : targetList) {

            vo.setAuthorName(userMap.get(vo.getAuthorId()).getNickname());

            vo.setCategory(categoryMap.get(vo.getCategoryId()));

            vo.setTagIds(postTagIdMap.get(vo.getId()));

            List<TagVo> tagVoList = tagVoConvertor.toViewVoList(tagService.findByFieldList(Tag::getId, vo.getTagIds()));
            vo.setTagList(tagVoList);
        }
    }
}
