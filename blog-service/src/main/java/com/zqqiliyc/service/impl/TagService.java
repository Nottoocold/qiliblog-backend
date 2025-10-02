package com.zqqiliyc.service.impl;

import com.zqqiliyc.domain.dto.CreateDto;
import com.zqqiliyc.domain.dto.UpdateDto;
import com.zqqiliyc.domain.entity.Tag;
import com.zqqiliyc.framework.web.event.EntityDeleteEvent;
import com.zqqiliyc.framework.web.spring.SpringUtils;
import com.zqqiliyc.repository.mapper.TagMapper;
import com.zqqiliyc.service.ITagService;
import com.zqqiliyc.service.base.AbstractBaseService;
import io.mybatis.provider.util.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author qili
 * @date 2025-06-01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TagService extends AbstractBaseService<Tag, Long, TagMapper> implements ITagService {

    @Override
    public Tag create(CreateDto<Tag> dto) {
        validateConstraintAndThrow(dto);
        Tag entity = dto.toEntity();
        validateBeforeCreateWithDB(Map.of(Tag::getName, entity.getName()), "标签名称已存在");
        validateBeforeCreateWithDB(Map.of(Tag::getSlug, entity.getSlug()), "标签 URL 友好标识符已存在");
        Assert.isTrue(baseMapper.insert(entity) == 1, "insert failed");
        return entity;
    }

    @Override
    public Tag update(UpdateDto<Tag> dto) {
        validateConstraintAndThrow(dto);
        Tag entity = findById(dto.getId());
        dto.fillEntity(entity);
        return update(entity);
    }

    @Override
    public Tag update(Tag entity) {
        Long[] excludeIds = {entity.getId()};
        validateBeforeUpdateWithDB(Map.of(Tag::getName, entity.getName()), "标签名称已存在", excludeIds);
        validateBeforeUpdateWithDB(Map.of(Tag::getSlug, entity.getSlug()), "标签 URL 友好标识符已存在", excludeIds);
        return super.update(entity);
    }

    @Override
    public int deleteById(Long id) {
        Tag entity = findById(id);
        SpringUtils.publishEvent(new EntityDeleteEvent<>(this, entity));
        return super.deleteById(id);
    }
}
