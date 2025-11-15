package com.zqqiliyc.biz.core.service.impl;

import com.zqqiliyc.biz.core.dto.article.ArticleDraftCreateDTO;
import com.zqqiliyc.biz.core.entity.Article;
import com.zqqiliyc.biz.core.repository.mapper.ArticleMapper;
import com.zqqiliyc.biz.core.service.IArticleService;
import com.zqqiliyc.biz.core.service.IRelArticleTagService;
import com.zqqiliyc.biz.core.service.base.AbstractBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author qili
 * @date 2025-06-02
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArticleService extends AbstractBaseService<Article, Long, ArticleMapper> implements IArticleService {
    private final IRelArticleTagService relArticleTagService;

    public ArticleService(IRelArticleTagService relArticleTagService) {
        super();
        this.relArticleTagService = relArticleTagService;
    }

    @Override
    public Article createDraft(ArticleDraftCreateDTO draftSaveDTO) {
        draftSaveDTO.setReadTime("少于1分钟");
        // 1. 创建文章
        Article article = create(draftSaveDTO);
        // 2. 保存文章标签
        List<Long> tagIds = draftSaveDTO.getTagIds();
        relArticleTagService.save(article.getId(), tagIds);
        return article;
    }
}
