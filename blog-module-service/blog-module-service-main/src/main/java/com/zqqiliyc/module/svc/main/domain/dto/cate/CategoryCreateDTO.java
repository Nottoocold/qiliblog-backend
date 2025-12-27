package com.zqqiliyc.module.svc.main.domain.dto.cate;

import cn.hutool.core.bean.BeanUtil;
import com.zqqiliyc.framework.web.domain.dto.CreateDTO;
import com.zqqiliyc.module.svc.main.domain.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-10-02
 */
@Getter
@Setter
@Schema(description = "创建分类 DTO")
public class CategoryCreateDTO implements CreateDTO<Category> {
    @NotBlank(message = "分类名称不能为空")
    @Schema(description = "分类名称")
    private String name;

    @NotBlank(message = "分类 URL 友好标识符不能为空")
    @Schema(description = "分类 URL 友好标识符")
    private String slug;

    @Schema(description = "分类描述")
    private String description;

    @Override
    public Category toEntity() {
        Category entity = new Category();
        BeanUtil.copyProperties(this, entity);
        return entity;
    }
}
