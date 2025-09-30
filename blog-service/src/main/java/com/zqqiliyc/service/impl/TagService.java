package com.zqqiliyc.service.impl;

import com.zqqiliyc.domain.dto.CreateDto;
import com.zqqiliyc.domain.dto.UpdateDto;
import com.zqqiliyc.domain.entity.Tag;
import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import com.zqqiliyc.framework.web.exception.ClientException;
import com.zqqiliyc.repository.mapper.TagMapper;
import com.zqqiliyc.service.ITagService;
import com.zqqiliyc.service.base.AbstractBaseService;
import io.mybatis.mapper.example.ExampleWrapper;
import io.mybatis.mapper.fn.Fn;
import io.mybatis.provider.util.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author qili
 * @date 2025-06-01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TagService extends AbstractBaseService<Tag, Long, TagMapper> implements ITagService {

    @Override
    public Tag create(CreateDto<Tag> dto) {
        Tag entity = dto.toEntity();
        // 校验唯一性
        // name
        validateForCreateOrUpdate(Map.of(Tag::getName, entity.getName()),
                () -> new ClientException(GlobalErrorDict.PARAM_ERROR, "标签名称已存在"),
                Collections.emptyList());
        // slug
        validateForCreateOrUpdate(Map.of(Tag::getSlug, entity.getSlug()),
                () -> new ClientException(GlobalErrorDict.PARAM_ERROR, "标签 URL 友好标识符已存在"),
                Collections.emptyList());
        Assert.isTrue(baseMapper.insert(entity) == 1, "insert failed");
        return entity;
    }

    @Override
    public Tag update(UpdateDto<Tag> dto) {
        Tag entity = findById(dto.getId());
        dto.fillEntity(entity);
        return update(entity);
    }

    @Override
    public Tag update(Tag entity) {
        // 校验唯一性
        // name
        validateForCreateOrUpdate(Map.of(Tag::getName, entity.getName()),
                () -> new ClientException(GlobalErrorDict.PARAM_ERROR, "标签名称已存在"),
                List.of(entity.getId()));
        // slug
        validateForCreateOrUpdate(Map.of(Tag::getSlug, entity.getSlug()),
                () -> new ClientException(GlobalErrorDict.PARAM_ERROR, "标签 URL 友好标识符已存在"),
                List.of(entity.getId()));
        return super.update(entity);
    }

    private void validateForCreateOrUpdate(Map<Fn<Tag, Object>, Object> conditions,
                                           Supplier<ClientException> supplier,
                                           List<Long> notInIds) {
        ExampleWrapper<Tag, Long> exampleWrapper = wrapper();
        for (Map.Entry<Fn<Tag, Object>, Object> entry : conditions.entrySet()) {
            exampleWrapper.eq(entry.getKey(), entry.getValue());
        }
        if (exampleWrapper.notIn(Tag::getId, notInIds).first().isPresent()) {
            throw supplier.get();
        }
    }
}
