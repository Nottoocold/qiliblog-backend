package com.zqqiliyc.biz.core.service.listener;

import cn.hutool.core.lang.Assert;
import com.zqqiliyc.biz.core.entity.Category;
import com.zqqiliyc.framework.web.event.EntityDeleteEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author qili
 * @date 2025-10-02
 */
@Component
@RequiredArgsConstructor
class CategoryEventListener {

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onDelete(EntityDeleteEvent<Category> event) {
        Category category = event.getEntity();
        Assert.isTrue(category.getPostCount() == 0, "分类下有文章，请先删除文章");
    }
}
