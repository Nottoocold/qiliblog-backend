package com.zqqiliyc.module.svc.main.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @author qili
 * @date 2025-11-15
 */
@Getter
@Setter
public class ArticleVo {
    @Schema(description = "文章ID")
    private Long id;
    @Schema(description = "文章标题")
    private String title;
    @Schema(description = "文章 URL 友好标识符 (必须唯一，用于生成永久链接)")
    private String slug;
    @Schema(description = "文章内容")
    private String content;
    @Schema(description = "文章摘要, 用于文章列表展示")
    private String summary;
    @Schema(description = "文章封面图片")
    private String coverImage;
    @Schema(description = "文章状态: draft(0草稿), published(1已发布), private(2私密)")
    private int status;
    @Schema(description = "文章分类ID")
    private Long categoryId;
    @Schema(description = "文章分类")
    private CategoryVo category;
    @Schema(description = "文章标签ID列表")
    private Collection<Long> tagIds;
    @Schema(description = "文章标签列表")
    private Collection<TagVo> tagList;
    @Schema(description = "文章作者ID")
    private Long authorId;
    @Schema(description = "文章作者")
    private String authorName;
    @Schema(description = "是否置顶: 0否, 1是")
    private int top;
    @Schema(description = "是否推荐: 0否, 1是")
    private int recommend;
    @Schema(description = "是否允许评论: 0不允许, 1允许")
    private int allowComment;
    @Schema(description = "需要定时发布时设置")
    private LocalDateTime publishAt;
    @Schema(description = "文章发布时间 (当状态变为 published(1) 时设置)")
    private LocalDateTime publishedTime;
    @Schema(description = "文章内容修改时间(当状态为 published(1) 时, 每次修改时设置)")
    private LocalDateTime modifiedTime;
    @Schema(description = "文章浏览次数(每次浏览时设置)")
    private int viewCount;
    @Schema(description = "文章点赞次数(每次点赞时设置)")
    private int likeCount;
    @Schema(description = "文章字数")
    private int wordCount;
    @Schema(description = "预计阅读时间,如：少于1分钟，3分钟，多于30分钟等")
    private String readTime;
}
