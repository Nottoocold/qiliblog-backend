package com.zqqiliyc.module.biz.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-10-02
 */
@Getter
@Setter
@Schema(description = "分类视图")
public class CategoryVo {
    @Schema(description = "分类 ID")
    private Long id;

    @Schema(description = "分类名称")
    private String name;

    @Schema(description = "分类 URL 友好标识符")
    private String slug;

    @Schema(description = "分类下的文章数量")
    private int postCount;

    @Schema(description = "分类描述")
    private String description;
}
