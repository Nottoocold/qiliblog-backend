package com.zqqiliyc.module.svc.main.service;

import com.zqqiliyc.framework.web.service.IBaseService;
import com.zqqiliyc.module.svc.main.domain.entity.Tag;

/**
 * @author qili
 * @date 2025-06-01
 */
public interface ITagService extends IBaseService<Tag, Long> {

    /**
     * 更新标签文章数
     *
     * @param tagId 标签ID
     * @param delta 变化量（正数增加，负数减少）
     */
    void updateTagPostCount(Long tagId, int delta);
}
