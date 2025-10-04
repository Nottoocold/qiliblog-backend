package com.zqqiliyc.biz.core.dto.cate;

import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.biz.core.dto.AbstractQueryDto;
import com.zqqiliyc.biz.core.entity.Category;
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
