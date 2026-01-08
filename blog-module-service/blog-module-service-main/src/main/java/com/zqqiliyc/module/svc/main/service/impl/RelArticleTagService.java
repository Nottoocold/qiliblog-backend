package com.zqqiliyc.module.svc.main.service.impl;

import com.zqqiliyc.framework.web.service.AbstractBaseService;
import com.zqqiliyc.module.svc.main.domain.entity.RelArticleTag;
import com.zqqiliyc.module.svc.main.mapper.RelArticleTagMapper;
import com.zqqiliyc.module.svc.main.service.IRelArticleTagService;
import com.zqqiliyc.module.svc.main.service.ITagService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author qili
 * @date 2025-06-02
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "relArticleTagCache")
@Transactional(rollbackFor = Exception.class)
public class RelArticleTagService extends AbstractBaseService<RelArticleTag, Long, RelArticleTagMapper> implements IRelArticleTagService {
    private final ITagService tagService;

    @Override
    @Cacheable(key = "'articleId:' + #articleId")
    public Set<Long> findByArticleId(Long articleId) {
        return wrapper().eq(RelArticleTag::getArticleId, articleId)
                .list().stream().map(RelArticleTag::getTagId).collect(Collectors.toSet());
    }

    @Override
    @CacheEvict(key = "'articleId:' + #articleId")
    public void deleteByArticleId(Long articleId) {
        List<Long> deleteTagIds = wrapper().eq(RelArticleTag::getArticleId, articleId)
                .list()
                .stream().map(RelArticleTag::getTagId).toList();

        wrapper().eq(RelArticleTag::getArticleId, articleId).delete();

        deleteTagIds.forEach(tagId -> tagService.updateTagPostCount(tagId, -1));
    }

    @Override
    @CacheEvict(key = "'articleId:' + #articleId")
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

        if (!deleteTagIds.isEmpty()) {
            deleteTagIds.forEach(tagId -> tagService.updateTagPostCount(tagId, -1));
        }
        if (!addTagIds.isEmpty()) {
            addTagIds.forEach(tagId -> tagService.updateTagPostCount(tagId, 1));
        }
    }

    @Override
    protected void beforeCreate(RelArticleTag entity) {

    }

    @Override
    protected void afterCreate(RelArticleTag entity) {

    }

    @Override
    protected void beforeUpdate(RelArticleTag entity) {

    }

    @Override
    protected void afterUpdate(RelArticleTag entity) {

    }

    @Override
    protected void beforeDelete(RelArticleTag entity) {

    }

    @Override
    protected void afterDelete(RelArticleTag entity) {

    }
}