package com.zqqiliyc.module.svc.main.domain.dto.article;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.zqqiliyc.framework.web.domain.dto.UpdateDTO;
import com.zqqiliyc.module.svc.main.domain.entity.Article;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章更新DTO
 *
 * @author qili
 * @date 2025-11-16
 */
@Getter
@Setter
@Schema(description = "文章更新DTO")
public class ArticleUpdateDTO implements UpdateDTO<Article> {

    @NotNull(message = "文章ID不能为空")
    @Schema(description = "文章ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @NotBlank(message = "文章标题不能为空")
    @Schema(description = "文章标题", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "文章 URL 友好标识符 (必须唯一，用于生成永久链接)")
    private String slug;

    @NotBlank(message = "文章内容不能为空")
    @Schema(description = "文章内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Schema(description = "文章摘要, 用于文章列表展示")
    private String summary;

    @Schema(description = "文章封面图片path", example = "blog/cover/2025/11/15/20251115150000.png")
    private String coverImage;

    @Positive(message = "文章分类ID不合法")
    @Schema(description = "文章分类ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long categoryId;

    @NotEmpty(message = "文章标签ID列表不能为空")
    @Schema(description = "文章标签ID列表")
    private List<Long> tagIds;

    @Schema(description = "是否置顶: 0否, 1是")
    private Integer top;

    @Schema(description = "是否推荐: 0否, 1是")
    private Integer recommend;

    @Schema(description = "是否允许评论: 0不允许, 1允许")
    private Integer allowComment;

    @Schema(description = "定时发布时间, 需要定时发布时设置")
    private LocalDateTime publishAt;

    @Override
    public void fillEntity(Article entity) {
        // 只复制非 null 的字段，避免覆盖不应修改的字段
        BeanUtil.copyProperties(this, entity, CopyOptions.create()
                .setIgnoreNullValue(true)
                .setIgnoreProperties("id", "tagIds"));
    }
}