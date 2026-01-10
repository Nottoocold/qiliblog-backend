package com.zqqiliyc.module.svc.main.scheduler;

import com.zqqiliyc.module.svc.main.domain.entity.Article;
import com.zqqiliyc.module.svc.main.service.IArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 文章定时发布调度器
 * <p>
 * 定时扫描需要发布的草稿文章，自动将到达发布时间的文章发布
 * </p>
 *
 * @author hallo
 * @date 2025-11-17
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleScheduler {
    private final IArticleService articleService;

    /**
     * 定时发布文章任务
     * <p>
     * 每分钟执行一次，查询所有满足以下条件的草稿文章并发布：
     * <ul>
     *   <li>文章状态为草稿(DRAFT, status=0)</li>
     *   <li>定时发布时间(publish_at)已到达且不为空</li>
     * </ul>
     * </p>
     */
    @Scheduled(cron = "0 * * * * ?")
    public void publishScheduledArticles() {
        try {
            // 查询所有需要发布的草稿文章
            List<Article> pendingArticles = articleService.findPendingPublishArticles();

            if (pendingArticles.isEmpty()) {
                return;
            }

            log.info("发现 {} 篇待发布的草稿文章", pendingArticles.size());

            // 逐个发布文章
            for (Article article : pendingArticles) {
                try {
                    articleService.publishArticle(article.getId());
                    log.info("文章发布成功: id={}, title={}", article.getId(), article.getTitle());
                } catch (Exception e) {
                    log.error("文章发布失败: id={}, title={}", article.getId(), article.getTitle(), e);
                }
            }
        } catch (Exception e) {
            log.error("定时发布文章任务执行失败", e);
        }
    }
}
