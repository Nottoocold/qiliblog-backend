package com.zqqiliyc.module.biz.service;

import com.zqqiliyc.module.biz.entity.RelArticleTag;
import com.zqqiliyc.module.biz.service.base.IBaseService;

import java.util.List;
import java.util.Set;

/**
 * @author qili
 * @date 2025-11-15
 */
public interface IRelArticleTagService extends IBaseService<RelArticleTag, Long> {

    /**
     * 根据文章ID查询标签ID列表, 该方法有缓存
     *
     * @param articleId 文章ID
     * @return 标签ID列表
     */
    Set<Long> findByArticleId(Long articleId);

    /**
     * 根据文章ID删除关系
     *
     * @param articleId 文章ID
     */
    void deleteByArticleId(Long articleId);

    /**
     * 保存文章标签关系
     *
     * @param articleId 文章ID
     * @param tagIds    标签ID列表
     */
    void save(Long articleId, List<Long> tagIds);
}
