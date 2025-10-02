package com.zqqiliyc.domain.dto.cate;

import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.domain.dto.AbstractQueryDto;
import com.zqqiliyc.domain.entity.Category;
import io.mybatis.mapper.example.Example;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-09-30
 */
@Getter
@Setter
public class CategoryQueryDto extends AbstractQueryDto<Category> {
    private String word;

    @Override
    protected void fillExample(Example<Category> example) {
        example.createCriteria()
                .andLike(StrUtil.isNotBlank(word), Category::getName, "%" + word + "%");
        example.or().andLike(StrUtil.isNotBlank(word), Category::getSlug, "%" + word + "%");
    }
}
