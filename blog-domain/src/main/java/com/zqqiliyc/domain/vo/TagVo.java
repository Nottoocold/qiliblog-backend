package com.zqqiliyc.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-09-30
 */
@Getter
@Setter
@Schema(description = "标签View")
public class TagVo {
    @Schema(description = "标签ID")
    private Long id;

    @Schema(description = "标签名")
    private String name;

    @Schema(description = "标签 URL 友好标识符 (必须唯一，如 java, spring-boot)")
    private String slug;

    @Schema(description = "标签下文章数")
    private int postCount;

    @Schema(description = "标签描述")
    private String description;
}
