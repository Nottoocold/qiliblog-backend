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
    @Schema(description = "是否延迟发布: false否, true是")
    private boolean delayPublish;

    @Schema(description = "延迟发布时间, 需要延迟发布时设置")
    private LocalDateTime publishAt;
}
