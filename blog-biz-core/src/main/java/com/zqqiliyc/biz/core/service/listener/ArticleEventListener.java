package com.zqqiliyc.biz.core.service.listener;

import com.zqqiliyc.biz.core.entity.Article;
import com.zqqiliyc.biz.core.entity.Category;
import com.zqqiliyc.biz.core.repository.mapper.CategoryMapper;
import com.zqqiliyc.framework.web.event.EntityCreateEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Optional;

/**
 * @author qili
 * @date 2025-11-16
 */
@Component
public class ArticleEventListener {
    private final CategoryMapper categoryMapper;

    public ArticleEventListener(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void beforeCommit(EntityCreateEvent<Article> event) {
        Article article = event.getEntity();
        article.setWordCount(article.getContent().length());
        Optional<Category> category = categoryMapper.selectByPrimaryKey(article.getCategoryId());
        if (category.isPresent()) {
            category.get().setPostCount(category.get().getPostCount() + 1);
            categoryMapper.updateByPrimaryKey(category.get());
        }
    }
}
