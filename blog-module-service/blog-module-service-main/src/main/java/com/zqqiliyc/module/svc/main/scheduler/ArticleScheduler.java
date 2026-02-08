package com.zqqiliyc.module.svc.main.scheduler;

import cn.hutool.core.collection.CollUtil;
import com.zqqiliyc.module.svc.main.domain.entity.Article;
import com.zqqiliyc.module.svc.main.service.IArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CountDownLatch;

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
    private final ThreadPoolTaskExecutor taskExecutor;

    @Value("${qiliblog.article.publish.threshold:50}")
    int thresholdPool;

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
    @Scheduled(cron = "1 * * * * ?")
    public void publishScheduledArticles() {
        try {
            // 查询所有需要发布的草稿文章
            List<Article> pendingArticles = articleService.findPendingPublishArticles();

            if (pendingArticles.isEmpty()) {
                return;
            }

            log.info("发现 {} 篇待发布的草稿文章", pendingArticles.size());

            boolean enablePool = pendingArticles.size() > thresholdPool;
            if (enablePool) {
                log.info("待发布文章数量较多，启用线程池处理");
                poolHandle(pendingArticles);
                return;
            }

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

    private void poolHandle(List<Article> pendingArticles) {
        List<List<Article>> articleLists = CollUtil.split(pendingArticles, thresholdPool);
        CountDownLatch latch = new CountDownLatch(articleLists.size());
        for (List<Article> articleList : articleLists) {
            taskExecutor.execute(() -> {
                for (Article article : articleList) {
                    articleService.publishArticle(article.getId());
                    log.info("文章发布成功: id={}, title={}", article.getId(), article.getTitle());
                }
                latch.countDown();
            });
        }
        try {
            latch.await();
            log.info("所有文章发布完成");
        } catch (InterruptedException e) {
            log.error("等待线程执行完成时发生中断", e);
        }
    }
}
