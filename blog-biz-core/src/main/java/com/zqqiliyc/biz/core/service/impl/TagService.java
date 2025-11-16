package com.zqqiliyc.biz.core.service.impl;

import cn.hutool.core.lang.Assert;
import com.zqqiliyc.biz.core.dto.CreateDTO;
import com.zqqiliyc.biz.core.dto.UpdateDTO;
import com.zqqiliyc.biz.core.entity.Tag;
import com.zqqiliyc.biz.core.repository.mapper.TagMapper;
import com.zqqiliyc.biz.core.service.ITagService;
import com.zqqiliyc.biz.core.service.base.AbstractBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

/**
 * @author qili
 * @date 2025-06-01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TagService extends AbstractBaseService<Tag, Long, TagMapper> implements ITagService {

    @Override
    public Tag create(CreateDTO<Tag> dto) {
        Tag entity = dto.toEntity();
        validateBeforeCreateWithDB(Map.of(Tag::getName, entity.getName()), "标签名称已存在");
        validateBeforeCreateWithDB(Map.of(Tag::getSlug, entity.getSlug()), "标签 URL 友好标识符已存在");
        Assert.isTrue(baseMapper.insert(entity) == 1, "insert failed");
        return entity;
    }

    @Override
    public Tag update(UpdateDTO<Tag> dto) {
        Tag entity = findById(dto.getId());
        dto.fillEntity(entity);
        Long[] excludeIds = {entity.getId()};
        validateBeforeUpdateWithDB(Map.of(Tag::getName, entity.getName()), "标签名称已存在", excludeIds);
        validateBeforeUpdateWithDB(Map.of(Tag::getSlug, entity.getSlug()), "标签 URL 友好标识符已存在", excludeIds);
        Assert.isTrue(baseMapper.updateByPrimaryKey(entity) == 1, "update failed");
        return entity;
    }

    @Override
    public void updateTagPostCount(Long tagId, int delta) {
        Optional<Tag> tag = baseMapper.selectByPrimaryKey(tagId);
        if (tag.isPresent()) {
            Tag tagEntity = tag.get();
            tagEntity.setPostCount(Math.max(0, tagEntity.getPostCount() + delta));
            baseMapper.updateByPrimaryKey(tagEntity);
        }
    }
}
