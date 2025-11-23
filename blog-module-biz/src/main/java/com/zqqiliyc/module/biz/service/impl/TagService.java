package com.zqqiliyc.module.biz.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import com.zqqiliyc.framework.web.exception.ClientException;
import com.zqqiliyc.module.biz.dto.CreateDTO;
import com.zqqiliyc.module.biz.dto.UpdateDTO;
import com.zqqiliyc.module.biz.entity.Tag;
import com.zqqiliyc.module.biz.repository.mapper.TagMapper;
import com.zqqiliyc.module.biz.service.ITagService;
import com.zqqiliyc.module.biz.service.base.AbstractBaseService;
import io.mybatis.mapper.fn.Fn;
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
        Fn<Tag, Object> field = Fn.field(baseMapper.entityClass(), Tag::getPostCount);
        String column = field.toColumn();
        String operator = delta >= 0 ? "+" : "-";
        baseMapper.wrapper()
                .eq(Tag::getId, tagId)
                .set(StrUtil.format("{} = {} {} {}", column, column, operator, Math.abs(delta)))
                .update();
    }

    @Override
    protected void beforeDelete(Tag tag) {
        Assert.isTrue(tag.getPostCount() == 0, () -> new ClientException(GlobalErrorDict.PARAM_ERROR, "标签下有文章，无法删除"));
    }
}
