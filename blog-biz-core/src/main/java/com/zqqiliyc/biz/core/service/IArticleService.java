package com.zqqiliyc.biz.core.service;

import com.zqqiliyc.biz.core.dto.article.ArticleDraftCreateDTO;
import com.zqqiliyc.biz.core.dto.article.ArticleUpdateDTO;
import com.zqqiliyc.biz.core.entity.Article;
import com.zqqiliyc.biz.core.service.base.IBaseService;

/**
 * @author qili
 * @date 2025-06-02
 */
public interface IArticleService extends IBaseService<Article, Long> {

    /**
     * 根据 slug 查询文章
     *
     * @param slug URL 友好标识符
     * @return 文章实体，不存在返回 null
     */
    Article findBySlug(String slug);

    /**
     * 保存草稿文章
     *
     * @param draftSaveDTO 草稿文章保存DTO
     * @return 保存后的文章实体
     */
    Article createDraft(ArticleDraftCreateDTO draftSaveDTO);

    /**
     * 更新文章
     *
     * @param updateDTO 更新DTO
     * @return 更新后的文章实体
     */
    Article updateArticle(ArticleUpdateDTO updateDTO);
}
