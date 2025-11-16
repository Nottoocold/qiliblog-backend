package com.zqqiliyc.biz.core.dto.cate;

import cn.hutool.core.bean.BeanUtil;
import com.zqqiliyc.biz.core.dto.UpdateDTO;
import com.zqqiliyc.biz.core.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-10-02
 */
@Getter
@Setter
@Schema(description = "更新分类 DTO")
public class CategoryUpdateDTO implements UpdateDTO<Category> {
    @NotNull(message = "分类 ID 不能为空")
    @Schema(description = "分类 ID")
    private Long id;

    @NotBlank(message = "分类名称不能为空")
    @Schema(description = "分类名称")
    private String name;

    @NotBlank(message = "分类 URL 友好标识符不能为空")
    @Schema(description = "分类 URL 友好标识符")
    private String slug;

    @Schema(description = "分类描述")
    private String description;

    @Override
    public void fillEntity(Category entity) {
        BeanUtil.copyProperties(this, entity, "id");
    }
}
