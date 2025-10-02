package com.zqqiliyc.service.listener;

import cn.hutool.core.lang.Assert;
import com.zqqiliyc.domain.entity.Tag;
import com.zqqiliyc.framework.web.event.EntityDeleteEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author qili
 * @date 2025-10-02
 */
@Component
class TagEventListener {

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onDelete(EntityDeleteEvent<Tag> event) {
        Tag tag = event.getEntity();
        Assert.isTrue(tag.getPostCount() == 0, "标签下有文章，请先删除文章");
    }
}
