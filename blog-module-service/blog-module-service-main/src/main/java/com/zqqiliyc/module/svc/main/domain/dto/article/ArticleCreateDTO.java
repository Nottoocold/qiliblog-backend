package com.zqqiliyc.module.svc.main.domain.dto.article;

import cn.hutool.core.bean.BeanUtil;
import com.zqqiliyc.framework.web.domain.dto.CreateDTO;
import com.zqqiliyc.module.svc.main.domain.entity.Article;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 文章新增基础DTO，根据后续业务不同，可以继承此DTO，并添加新的字段
 *
 * @author qili
 * @date 2025-11-15
 */
@Getter
@Setter
@Schema(description = "文章新增基础DTO")
public class ArticleCreateDTO implements CreateDTO<Article> {
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

    @Schema(description = "文章作者ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long authorId;

    @Positive(message = "文章分类ID不合法")
    @Schema(description = "文章分类ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long categoryId;

    @NotEmpty(message = "文章标签ID列表不能为空")
    @Schema(description = "文章标签ID列表")
    private List<Long> tagIds;

    @Schema(description = "是否置顶: 0否, 1是")
    private int top;

    @Schema(description = "是否推荐: 0否, 1是")
    private int recommend;

    @Schema(description = "预计阅读时间,如：少于1分钟，3分钟，多于30分钟等")
    private String readTime;

    @Override
    public Article toEntity() {
        Article article = new Article();
        BeanUtil.copyProperties(this, article);
        return article;
    }
}
