package com.zqqiliyc.module.svc.main.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import com.zqqiliyc.framework.web.exception.ClientException;
import com.zqqiliyc.framework.web.service.AbstractBaseService;
import com.zqqiliyc.module.svc.main.domain.entity.Tag;
import com.zqqiliyc.module.svc.main.mapper.TagMapper;
import com.zqqiliyc.module.svc.main.service.ITagService;
import io.mybatis.mapper.fn.Fn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author qili
 * @date 2025-06-01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TagService extends AbstractBaseService<Tag, Long, TagMapper> implements ITagService {

    @Override
    protected void beforeCreate(Tag entity) {
        if (wrapper().eq(Tag::getName, entity.getName()).one().isPresent()) {
            throw new ClientException(GlobalErrorDict.PARAM_ERROR, "标签名称已存在");
        }
        if (wrapper().eq(Tag::getSlug, entity.getSlug()).one().isPresent()) {
            throw new ClientException(GlobalErrorDict.PARAM_ERROR, "标签 URL 友好标识符已存在");
        }
    }

    @Override
    protected void afterCreate(Tag entity) {

    }

    @Override
    protected void beforeUpdate(Tag entity) {
        if (wrapper().eq(Tag::getName, entity.getName())
                .ne(Tag::getId, entity.getId())
                .one().isPresent()) {
            throw new ClientException(GlobalErrorDict.PARAM_ERROR, "标签名称已存在");
        }
        if (wrapper().eq(Tag::getSlug, entity.getSlug())
                .ne(Tag::getId, entity.getId())
                .one().isPresent()) {
            throw new ClientException(GlobalErrorDict.PARAM_ERROR, "标签 URL 友好标识符已存在");
        }
    }

    @Override
    protected void afterUpdate(Tag entity) {

    }

    @Override
    public void updateTagPostCount(Long tagId, int delta) {
        Fn<Tag, Object> field = Fn.field(baseMapper.entityClass(), Tag::getPostCount);
        String column = field.toColumn();
        String operator = delta >= 0 ? "+" : "-";
        baseMapper.wrapper()
                .eq(Tag::getId, tagId)
                .set(StrUtil.format("{} = {} {} {}", column, column, operator, Math.abs(delta)))
                .set(Tag::getUpdateTime, LocalDateTime.now())
                .update();
    }

    @Override
    protected void beforeDelete(Tag tag) {
        Assert.isTrue(tag.getPostCount() == 0, () -> new ClientException(GlobalErrorDict.PARAM_ERROR, "标签下有文章，无法删除"));
    }

    @Override
    protected void afterDelete(Tag entity) {

    }
}
