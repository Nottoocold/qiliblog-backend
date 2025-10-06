package com.zqqiliyc.biz.core.dto.tag;

import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.biz.core.dto.AbstractQueryDTO;
import com.zqqiliyc.biz.core.entity.Tag;
import io.mybatis.mapper.example.Example;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-09-30
 */
@Getter
@Setter
public class TagQueryDTO extends AbstractQueryDTO<Tag> {
    private String word;

    @Override
    protected void fillExample(Example<Tag> example) {
        example.createCriteria()
                .andLike(StrUtil.isNotBlank(word), Tag::getName, "%" + word + "%");
        example.or().andLike(StrUtil.isNotBlank(word), Tag::getSlug, "%" + word + "%");
    }
}
