package com.zqqiliyc.module.svc.main.domain.dto.tag;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.framework.web.domain.dto.AbstractQueryDTO;
import com.zqqiliyc.module.svc.main.domain.entity.Tag;
import io.mybatis.mapper.example.Example;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author qili
 * @date 2025-09-30
 */
@Getter
@Setter
public class TagQueryDTO extends AbstractQueryDTO<Tag> {
    @Schema(description = "标签ID列表")
    private List<Long> ids;

    @Schema(description = "关键字,name,slug")
    private String word;

    @Override
    protected void fillExample(Example<Tag> example) {
        Example.Criteria<Tag> criteria = example.createCriteria();
        criteria.andIn(CollUtil.isNotEmpty(ids), Tag::getId, ids);
        if (StrUtil.isNotBlank(word)) {
            criteria.andOr(List.of(
                    example.orPart().like(Tag::getName, "%" + word + "%"),
                    example.orPart().like(Tag::getSlug, "%" + word + "%")
            ));
        }
    }
}
