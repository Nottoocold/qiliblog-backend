package com.zqqiliyc.biz.core.service.impl;

import cn.hutool.core.lang.Assert;
import com.zqqiliyc.biz.core.dto.article.ArticleDraftCreateDTO;
import com.zqqiliyc.biz.core.entity.Article;
import com.zqqiliyc.biz.core.repository.mapper.ArticleMapper;
import com.zqqiliyc.biz.core.service.IArticleService;
import com.zqqiliyc.biz.core.service.IRelArticleTagService;
import com.zqqiliyc.biz.core.service.base.AbstractBaseService;
import com.zqqiliyc.framework.web.event.EntityCreateEvent;
import com.zqqiliyc.framework.web.spring.SpringUtils;
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
        Article article = draftSaveDTO.toEntity();
        // 1. 创建文章
        SpringUtils.publishEvent(new EntityCreateEvent<>(this, article));
        Assert.isTrue(baseMapper.insert(article) == 1, "insert article failed");
        // 2. 保存文章标签
        List<Long> tagIds = draftSaveDTO.getTagIds();
        relArticleTagService.save(article.getId(), tagIds);
        return article;
    }
}
