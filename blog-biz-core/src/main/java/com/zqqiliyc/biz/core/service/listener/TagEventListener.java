package com.zqqiliyc.biz.core.service.listener;

import cn.hutool.core.lang.Assert;
import com.zqqiliyc.biz.core.entity.Tag;
import com.zqqiliyc.biz.core.service.ITagService;
import com.zqqiliyc.framework.web.event.EntityDeleteEvent;
import com.zqqiliyc.framework.web.event.TagCountChangeEvent;
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
class TagEventListener {
    private final ITagService tagService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onDelete(EntityDeleteEvent<Tag> event) {
        Tag tag = event.getEntity();
        Assert.isTrue(tag.getPostCount() == 0, "标签下有文章，请先删除文章");
    }

    /**
     * 处理标签文章数变化事件
     * 在事务提交前更新标签的文章计数
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onTagCountChange(TagCountChangeEvent event) {
        // 增加文章数
        if (event.getIncrementTagIds() != null && !event.getIncrementTagIds().isEmpty()) {
            for (Long tagId : event.getIncrementTagIds()) {
                tagService.updateTagPostCount(tagId, 1);
            }
        }

        // 减少文章数
        if (event.getDecrementTagIds() != null && !event.getDecrementTagIds().isEmpty()) {
            for (Long tagId : event.getDecrementTagIds()) {
                tagService.updateTagPostCount(tagId, -1);
            }
        }
    }
}
