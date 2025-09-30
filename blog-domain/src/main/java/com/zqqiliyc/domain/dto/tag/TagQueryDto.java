package com.zqqiliyc.domain.dto.tag;

import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.domain.dto.AbstractQueryDto;
import com.zqqiliyc.domain.entity.Tag;
import io.mybatis.mapper.example.Example;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-09-30
 */
@Getter
@Setter
public class TagQueryDto extends AbstractQueryDto<Tag> {
    private String word;

    @Override
    protected void fillExample(Example<Tag> example) {
        example.createCriteria()
                .andLike(StrUtil.isNotBlank(word), Tag::getName, "%" + word + "%");
    }
}
