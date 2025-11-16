package com.zqqiliyc.biz.core.service.listener;

import com.zqqiliyc.biz.core.entity.Article;
import com.zqqiliyc.biz.core.service.ICategoryService;
import com.zqqiliyc.framework.web.event.EntityCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author qili
 * @date 2025-11-16
 */
@Component
@RequiredArgsConstructor
public class ArticleEventListener {
    private final ICategoryService categoryService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void beforeCommit(EntityCreateEvent<Article> event) {
        Article article = event.getEntity();
        categoryService.updateCategoryPostCount(article.getCategoryId(), 1);
    }
}
