package com.zqqiliyc.module.svc.main.domain.dto.article;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 保存草稿文章DTO
 *
 * @author qili
 * @date 2025-11-15
 */
@Getter
@Setter
public class ArticleDraftCreateDTO extends ArticleCreateDTO {

    @Schema(description = "定时发布时间（分钟精度）, 设置后到点自动发布, null 表示不定时")
    private LocalDateTime publishAt;
}
