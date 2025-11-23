package com.zqqiliyc.module.biz.dto.cate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.module.biz.dto.AbstractQueryDTO;
import com.zqqiliyc.module.biz.entity.Category;
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
public class CategoryQueryDTO extends AbstractQueryDTO<Category> {
    @Schema(description = "分类ID列表")
    private List<Long> ids;

    @Schema(description = "关键字,name,slug")
    private String word;

    @Override
    protected void fillExample(Example<Category> example) {
        Example.Criteria<Category> criteria = example.createCriteria();
        criteria.andIn(CollUtil.isNotEmpty(ids), Category::getId, ids);
        if (StrUtil.isNotBlank(word)) {
            criteria.andOr(List.of(
                    example.orPart().like(Category::getName, "%" + word + "%"),
                    example.orPart().like(Category::getSlug, "%" + word + "%")
            ));
        }
    }
}
