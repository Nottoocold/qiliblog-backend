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
     * <p>
     * 业务逻辑说明:
     * <ol>
     *   <li>验证参数合法性：
     *     <ul>
     *       <li>若启用延迟发布，检查发布时间是否设置且不早于当前时间</li>
     *     </ul>
     *   </li>
     *   <li>构建文章实体对象：
     *     <ul>
     *       <li>计算并设置文章字数</li>
     *       <li>设置文章状态为草稿(DRAFT)</li>
     *       <li>设置当前用户为作者</li>
     *       <li>生成或验证 slug 标识符</li>
     *     </ul>
     *   </li>
     *   <li>持久化数据到数据库：
     *     <ul>
     *       <li>向 blog_article 表插入文章记录</li>
     *       <li>向 rel_article_tag 表插入文章与标签的关联关系</li>
     *       <li>向 blog_tags 表更新post_count列</li>
     *     </ul>
     *   </li>
     * </ol>
     * </p>
     * <p>
     * 操作的数据表:
     * <ul>
     *   <li>blog_article: 存储文章基本信息</li>
     *   <li>rel_article_tag: 存储文章与标签的多对多关联关系</li>
     *   <li>blog_tags: 更新文章计数信息</li>
     * </ul>
     * </p>
     *
     * @param draftSaveDTO 草稿文章保存DTO，包含文章标题、内容、分类、标签等信息
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
