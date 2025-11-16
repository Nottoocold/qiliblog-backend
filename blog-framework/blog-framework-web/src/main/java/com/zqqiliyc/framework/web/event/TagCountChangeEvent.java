package com.zqqiliyc.framework.web.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * 标签文章数变化事件
 * 当文章与标签的关联关系发生变化时触发，用于更新标签的文章计数
 *
 * @author qili
 * @date 2025-11-16
 */
@Getter
public class TagCountChangeEvent extends ApplicationEvent {

    /**
     * 需要增加文章数的标签ID列表
     */
    private final List<Long> incrementTagIds;

    /**
     * 需要减少文章数的标签ID列表
     */
    private final List<Long> decrementTagIds;

    public TagCountChangeEvent(Object source, List<Long> incrementTagIds, List<Long> decrementTagIds) {
        super(source);
        this.incrementTagIds = incrementTagIds;
        this.decrementTagIds = decrementTagIds;
    }
}
