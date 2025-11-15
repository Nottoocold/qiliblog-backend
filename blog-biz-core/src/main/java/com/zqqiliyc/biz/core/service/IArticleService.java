package com.zqqiliyc.biz.core.service;

import com.zqqiliyc.biz.core.dto.article.ArticleDraftCreateDTO;
import com.zqqiliyc.biz.core.entity.Article;
import com.zqqiliyc.biz.core.service.base.IBaseService;

/**
 * @author qili
 * @date 2025-06-02
 */
public interface IArticleService extends IBaseService<Article, Long> {

    Article createDraft(ArticleDraftCreateDTO draftSaveDTO);
}
