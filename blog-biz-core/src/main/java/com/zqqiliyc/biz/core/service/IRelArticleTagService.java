package com.zqqiliyc.biz.core.service;

import com.zqqiliyc.biz.core.entity.RelArticleTag;
import com.zqqiliyc.biz.core.service.base.IBaseService;

import java.util.List;
import java.util.Set;

/**
 * @author qili
 * @date 2025-11-15
 */
public interface IRelArticleTagService extends IBaseService<RelArticleTag, Long> {

    Set<Long> findByArticleId(Long articleId);

    void save(Long articleId, List<Long> tagIds);
}
