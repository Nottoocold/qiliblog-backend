package com.zqqiliyc.biz.core.service.impl;

import cn.hutool.core.lang.Assert;
import com.zqqiliyc.biz.core.dto.article.ArticleDraftCreateDTO;
import com.zqqiliyc.biz.core.dto.article.ArticleUpdateDTO;
import com.zqqiliyc.biz.core.entity.Article;
import com.zqqiliyc.biz.core.repository.mapper.ArticleMapper;
import com.zqqiliyc.biz.core.service.IArticleService;
import com.zqqiliyc.biz.core.service.ICategoryService;
import com.zqqiliyc.biz.core.service.IRelArticleTagService;
import com.zqqiliyc.biz.core.service.base.AbstractBaseService;
import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import com.zqqiliyc.framework.web.event.EntityCreateEvent;
import com.zqqiliyc.framework.web.exception.ClientException;
import com.zqqiliyc.framework.web.spring.SpringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author qili
 * @date 2025-06-02
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArticleService extends AbstractBaseService<Article, Long, ArticleMapper> implements IArticleService {
    private final IRelArticleTagService relArticleTagService;
    private final ICategoryService categoryService;

    public ArticleService(IRelArticleTagService relArticleTagService, ICategoryService categoryService) {
        this.relArticleTagService = relArticleTagService;
        this.categoryService = categoryService;
    }

    @Override
    public Article createDraft(ArticleDraftCreateDTO draftSaveDTO) {
        draftSaveDTO.setReadTime("少于1分钟");
        Article article = draftSaveDTO.toEntity();
        // 1. 创建文章
        SpringUtils.publishEvent(new EntityCreateEvent<>(this, article));
        Assert.isTrue(baseMapper.insert(article) == 1, "insert article failed");
        // 2. 保存文章标签
        List<Long> tagIds = draftSaveDTO.getTagIds();
        relArticleTagService.save(article.getId(), tagIds);
        return article;
    }

    @Override
    public Article updateArticle(ArticleUpdateDTO updateDTO) {
        // 1. 查询文章是否存在
        Article article = findById(updateDTO.getId());
        if (article == null) {
            throw new ClientException(GlobalErrorDict.PARAM_ERROR, "文章不存在");
        }

        // 2. 检查分类是否变更，更新分类文章计数
        Long oldCategoryId = article.getCategoryId();
        Long newCategoryId = updateDTO.getCategoryId();
        if (newCategoryId != null && !newCategoryId.equals(oldCategoryId)) {
            categoryService.updateCategoryPostCount(oldCategoryId, -1);
            categoryService.updateCategoryPostCount(newCategoryId, 1);
        }

        // 3. 填充更新字段
        updateDTO.fillEntity(article);

        // 4. 重新计算字数
        if (updateDTO.getContent() != null) {
            article.setWordCount(updateDTO.getContent().length());
        }

        // 5. 如果文章已发布，更新内容修改时间
        if (article.getStatus() == 1) {
            article.setModifiedTime(LocalDateTime.now());
        }

        // 6. 更新文章
        Assert.isTrue(baseMapper.updateByPrimaryKey(article) == 1, "更新文章失败");

        // 7. 更新文章标签关联
        List<Long> tagIds = updateDTO.getTagIds();
        if (tagIds != null && !tagIds.isEmpty()) {
            relArticleTagService.save(article.getId(), tagIds);
        }

        return article;
    }
}
