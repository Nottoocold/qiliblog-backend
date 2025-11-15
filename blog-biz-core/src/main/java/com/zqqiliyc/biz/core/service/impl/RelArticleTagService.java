package com.zqqiliyc.biz.core.service.impl;

import com.zqqiliyc.biz.core.entity.RelArticleTag;
import com.zqqiliyc.biz.core.repository.mapper.RelArticleTagMapper;
import com.zqqiliyc.biz.core.service.IRelArticleTagService;
import com.zqqiliyc.biz.core.service.base.AbstractBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author qili
 * @date 2025-06-02
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RelArticleTagService extends AbstractBaseService<RelArticleTag, Long, RelArticleTagMapper> implements IRelArticleTagService {

    @Override
    public void save(Long articleId, List<Long> tagIds) {
        List<RelArticleTag> exists = baseMapper.wrapper()
                .eq(RelArticleTag::getArticleId, articleId)
                .list();

        // 数据库中有，但参数中没有，需要删除
        List<Long> existsTagIds = exists.stream().map(RelArticleTag::getTagId).toList();
        List<Long> deleteTagIds = existsTagIds.stream().filter(tagId -> !tagIds.contains(tagId)).toList();
        if (!deleteTagIds.isEmpty()) {
            baseMapper.wrapper()
                    .eq(RelArticleTag::getArticleId, articleId)
                    .in(RelArticleTag::getTagId, deleteTagIds)
                    .delete();
        }

        // 数据库中没有，但参数中有，需要新增
        List<Long> addTagIds = tagIds.stream().filter(tagId -> !existsTagIds.contains(tagId)).toList();

        addTagIds.stream().map(tagId -> {
            RelArticleTag relArticleTag = new RelArticleTag();
            relArticleTag.setArticleId(articleId);
            relArticleTag.setTagId(tagId);
            return relArticleTag;
        }).forEach(entity -> baseMapper.insert(entity));
    }
}