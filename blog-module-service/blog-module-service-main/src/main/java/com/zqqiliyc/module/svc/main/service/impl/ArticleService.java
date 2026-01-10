package com.zqqiliyc.module.svc.main.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import com.zqqiliyc.framework.web.event.EntityCreateEvent;
import com.zqqiliyc.framework.web.exception.ClientException;
import com.zqqiliyc.framework.web.service.AbstractBaseService;
import com.zqqiliyc.framework.web.spring.SpringUtils;
import com.zqqiliyc.module.svc.main.dict.ArticleStatus;
import com.zqqiliyc.module.svc.main.domain.dto.article.ArticleDraftCreateDTO;
import com.zqqiliyc.module.svc.main.domain.dto.article.ArticleUpdateDTO;
import com.zqqiliyc.module.svc.main.domain.entity.Article;
import com.zqqiliyc.module.svc.main.mapper.ArticleMapper;
import com.zqqiliyc.module.svc.main.service.IArticleService;
import com.zqqiliyc.module.svc.main.service.ICategoryService;
import com.zqqiliyc.module.svc.main.service.IRelArticleTagService;
import com.zqqiliyc.module.svc.main.service.ITagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author qili
 * @date 2025-06-02
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class ArticleService extends AbstractBaseService<Article, Long, ArticleMapper> implements IArticleService {
    private final IRelArticleTagService relArticleTagService;
    private final ICategoryService categoryService;
    private final ITagService tagService;

    @Override
    public Article findBySlug(String slug) {
        return wrapper().eq(Article::getSlug, slug).one().orElse(null);
    }

    @Override
    public Article createDraft(ArticleDraftCreateDTO draftSaveDTO) {
        if (draftSaveDTO.isDelayPublish()) {
            // 延迟发布
            if (draftSaveDTO.getPublishAt() == null) {
                throw new ClientException(GlobalErrorDict.PARAM_ERROR, "请选择发布时间");
            }
            if (draftSaveDTO.getPublishAt().isBefore(LocalDateTime.now())) {
                throw new ClientException(GlobalErrorDict.PARAM_ERROR, "发布时间不能早于当前时间");
            }
        }
        Article article = draftSaveDTO.toEntity();
        article.setWordCount(article.getContent().length());
        article.setModifiedTime(LocalDateTime.now());
        article.setStatus(ArticleStatus.DRAFT.intVal());
        if (StrUtil.isBlank(article.getSlug())) {
            // 自动生成唯一的 slug 标识
            article.setSlug(generateUniqueSlug());
        } else if (!article.getSlug().matches("^[a-zA-Z0-9-]+$")) {
            // slug 只允许包含字母、数字和连字符
            throw new ClientException(GlobalErrorDict.PARAM_ERROR, "slug 只允许包含字母、数字和连字符");
        }
        // 1. 创建文章
        SpringUtils.publishEvent(new EntityCreateEvent<>(this, article));
        Assert.isTrue(baseMapper.insert(article) == 1, () -> new ClientException(GlobalErrorDict.SERVER_ERROR, "创建文章失败"));
        // 2. 保存文章标签并更新计数
        List<Long> tagIds = draftSaveDTO.getTagIds();
        if (tagIds != null && !tagIds.isEmpty()) {
            relArticleTagService.save(article.getId(), tagIds);
            // 更新标签文章计数
            tagIds.forEach(tagId -> tagService.updateTagPostCount(tagId, 1));
        }
        // 3. 更新分类文章计数
        categoryService.updateCategoryPostCount(article.getCategoryId(), 1);
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

        // 5. 更新内容修改时间
        article.setModifiedTime(LocalDateTime.now());

        // 6. 更新文章
        Assert.isTrue(baseMapper.updateByPrimaryKey(article) == 1, () -> new ClientException(GlobalErrorDict.SERVER_ERROR, "更新文章失败"));

        // 7. 更新文章标签关联
        List<Long> tagIds = updateDTO.getTagIds();
        if (tagIds != null && !tagIds.isEmpty()) {
            relArticleTagService.save(article.getId(), tagIds);
        }

        return article;
    }

    @Override
    public void publishArticle(Long articleId) {
        // 1. 查询文章是否存在
        Article article = findById(articleId);
        if (article == null) {
            throw new ClientException(GlobalErrorDict.PARAM_ERROR, "文章不存在");
        }

        // 2. 检查文章状态是否为草稿
        if (article.getStatus() != ArticleStatus.DRAFT.intVal()) {
            return;
        }

        // 3. 更新文章状态为已发布
        article.setStatus(ArticleStatus.PUBLISHED.intVal());
        article.setPublishedTime(LocalDateTime.now());
        article.setPublishAt(null); // 清空定时发布时间

        // 4. 更新文章
        Assert.isTrue(baseMapper.updateByPrimaryKey(article) == 1,
            () -> new ClientException(GlobalErrorDict.SERVER_ERROR, "发布文章失败"));
    }

    @Override
    public List<Article> findPendingPublishArticles() {
        return wrapper()
            .eq(Article::getStatus, ArticleStatus.DRAFT.intVal())
            .isNotNull(Article::getPublishAt)
            .le(Article::getPublishAt, LocalDateTime.now())
            .list();
    }

    @Override
    protected void beforeCreate(Article entity) {

    }

    @Override
    protected void afterCreate(Article entity) {

    }

    @Override
    protected void beforeUpdate(Article entity) {

    }

    @Override
    protected void afterUpdate(Article entity) {

    }

    @Override
    protected void beforeDelete(Article entity) {

    }

    @Override
    protected void afterDelete(Article entity) {
        categoryService.updateCategoryPostCount(entity.getCategoryId(), -1);
        relArticleTagService.deleteByArticleId(entity.getId());
    }

    /**
     * 生成唯一的 slug 标识
     * <p>
     * 格式: {时间戳}-{8位随机字符}，示例: 20251116143025-a3Bx9Kp2
     * <p>
     * 支持 10w+ 文章数量:
     * <ul>
     *   <li>时间戳精确到秒，保证时间维度唯一性</li>
     *   <li>8位Base62随机字符，提供 62^8 = 218万亿 种组合</li>
     *   <li>数据库唯一性检查 + 重试机制</li>
     * </ul>
     *
     * @return 唯一的 slug 字符串
     */
    private String generateUniqueSlug() {
        int maxRetries = 3;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        for (int i = 0; i < maxRetries; i++) {
            // 生成时间戳部分（格式：yyyyMMddHHmmss）
            String timestamp = LocalDateTime.now().format(formatter);

            // 生成8位随机字符（Base62：0-9a-zA-Z）
            String randomPart = RandomUtil.randomString(8);

            String slug = timestamp + "-" + randomPart;

            // 检查数据库中是否已存在
            Article existingArticle = findBySlug(slug);
            if (existingArticle == null) {
                return slug;
            }

            // 如果重复，添加短暂延迟后重试
            if (i < maxRetries - 1) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        // 如果所有重试都失败，使用 UUID 作为后备方案（去掉横线）
        return UUID.randomUUID().toString().replace("-", "");
    }
}
