package com.zqqiliyc.module.svc.main.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import com.zqqiliyc.framework.web.exception.ClientException;
import com.zqqiliyc.framework.web.service.AbstractBaseService;
import com.zqqiliyc.module.svc.main.dict.ArticleStatus;
import com.zqqiliyc.module.svc.main.domain.dto.article.ArticleDraftCreateDTO;
import com.zqqiliyc.module.svc.main.domain.dto.article.ArticleUpdateDTO;
import com.zqqiliyc.module.svc.main.domain.entity.Article;
import com.zqqiliyc.module.svc.main.mapper.ArticleMapper;
import com.zqqiliyc.module.svc.main.service.IArticleService;
import com.zqqiliyc.module.svc.main.service.ICategoryService;
import com.zqqiliyc.module.svc.main.service.IRelArticleTagService;
import io.mybatis.mapper.fn.Fn;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author qili
 * @date 2025-06-02
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class ArticleService extends AbstractBaseService<Article, Long, ArticleMapper> implements IArticleService {
    private static final Pattern PATTERN_SLUG = Pattern.compile("^[a-zA-Z0-9-]+$");
    private final IRelArticleTagService relArticleTagService;
    private final ICategoryService categoryService;

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
            // 格式化发布时间
            draftSaveDTO.setPublishAt(draftSaveDTO.getPublishAt().withNano(0).withSecond(0));
        }
        Article article = draftSaveDTO.toEntity();
        // 基类创建：beforeCreate 钩子负责 slug、字数、状态等实体准备
        create(article);
        // 保存文章标签, 方法内部会自动更新标签的文章计数
        List<Long> tagIds = draftSaveDTO.getTagIds();
        if (tagIds != null && !tagIds.isEmpty()) {
            relArticleTagService.save(article.getId(), tagIds);
        }
        syncCategoryCount(null, article.getCategoryId());
        return article;
    }

    @Override
    public Article updateArticle(ArticleUpdateDTO updateDTO) {
        Article article = findById(updateDTO.getId());
        if (article == null) {
            throw new ClientException(GlobalErrorDict.PARAM_ERROR, "文章不存在");
        }
        Long oldCategoryId = article.getCategoryId();
        updateDTO.fillEntity(article);
        // 基类更新：beforeUpdate 钩子负责字数与修改时间
        update(article);
        syncCategoryCount(oldCategoryId, article.getCategoryId());
        // 更新文章标签关联, 方法内部会自动更新标签的文章计数
        List<Long> tagIds = updateDTO.getTagIds();
        if (tagIds != null && !tagIds.isEmpty()) {
            relArticleTagService.save(article.getId(), tagIds);
        }
        return article;
    }

    @Override
    public void publishArticle(Long articleId) {
        Class<Article> entityClass = baseMapper.entityClass();
        // 1. 查询文章是否存在
        Optional<Article> article = wrapper()
                .select(Fn.field(entityClass, Article::getId),
                        Fn.field(entityClass, Article::getTitle),
                        Fn.field(entityClass, Article::getStatus))
                .eq(Article::getId, articleId).one();
        if (article.isEmpty()) {
            throw new ClientException(GlobalErrorDict.PARAM_ERROR, "文章不存在");
        }

        // 2. 检查文章状态
        if (article.get().getStatus() != ArticleStatus.DRAFT.intVal()) {
            return;
        }
        if (article.get().getStatus() == ArticleStatus.PUBLISHED.intVal()) {
            return;
        }

        // 3. 更新文章状态为已发布
        LocalDateTime now = LocalDateTime.now();
        int updated = wrapper()
                .set(Article::getStatus, ArticleStatus.PUBLISHED.intVal())
                .set(Article::getPublishedTime, now)
                .set(Article::getPublishAt, null)
                .set(Article::getModifiedTime, now)
                .set(Article::getUpdateTime, now)
                .eq(Article::getId, articleId).update();
        Assert.isTrue(updated == 1, () -> new ClientException(GlobalErrorDict.SERVER_ERROR, "发布文章失败"));
    }

    @Override
    public List<Article> findPendingPublishArticles() {
        Class<Article> entityClass = baseMapper.entityClass();
        return wrapper()
                .select(Fn.field(entityClass, Article::getId),
                        Fn.field(entityClass, Article::getCreateTime),
                        Fn.field(entityClass, Article::getUpdateTime),
                        Fn.field(entityClass, Article::getTitle),
                        Fn.field(entityClass, Article::getStatus),
                        Fn.field(entityClass, Article::getPublishAt))
            .eq(Article::getStatus, ArticleStatus.DRAFT.intVal())
            .isNotNull(Article::getPublishAt)
                .le(Article::getPublishAt, LocalDateTime.now().withNano(0).withSecond(0))
            .list();
    }

    @Override
    protected void beforeCreate(Article entity) {
        if (StrUtil.isBlank(entity.getSlug())) {
            // 自动生成唯一的 slug 标识
            entity.setSlug(generateUniqueSlug());
        } else if (!PATTERN_SLUG.matcher(entity.getSlug()).matches()) {
            // slug 只允许包含字母、数字和连字符
            throw new ClientException(GlobalErrorDict.PARAM_ERROR, "slug 只允许包含字母、数字和连字符");
        }
        entity.setWordCount(entity.getContent().length());
        entity.setModifiedTime(LocalDateTime.now());
        entity.setStatus(ArticleStatus.DRAFT.intVal());
    }

    @Override
    protected void beforeUpdate(Article entity) {
        if (entity.getContent() != null) {
            entity.setWordCount(entity.getContent().length());
        }
        entity.setModifiedTime(LocalDateTime.now());
    }

    /**
     * 分类文章计数守恒：文章在分类间迁移（含创建/删除）时调用，旧分类 -1、新分类 +1，null 表示无
     */
    private void syncCategoryCount(Long oldCategoryId, Long newCategoryId) {
        if (Objects.equals(oldCategoryId, newCategoryId)) {
            return;
        }
        if (oldCategoryId != null) {
            categoryService.updateCategoryPostCount(oldCategoryId, -1);
        }
        if (newCategoryId != null) {
            categoryService.updateCategoryPostCount(newCategoryId, 1);
        }
    }
    @Override
    protected void afterDelete(Article entity) {
        syncCategoryCount(entity.getCategoryId(), null);
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
