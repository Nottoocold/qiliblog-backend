package com.zqqiliyc.module.biz.entity;

import io.mybatis.provider.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author qili
 * @date 2025-06-02
 */
@Getter
@Setter
@Entity.Table("blog_article")
public class Article extends BaseEntity {
    @Entity.Column(value = "title", remark = "文章标题")
    private String title;

    @Entity.Column(value = "slug", remark = "文章 URL 友好标识符 (必须唯一，用于生成永久链接)")
    private String slug;

    @Entity.Column(value = "content", remark = "文章内容")
    private String content;

    @Entity.Column(value = "summary", remark = "文章摘要, 用于文章列表展示")
    private String summary;

    @Entity.Column(value = "cover_image", remark = "文章封面图片")
    private String coverImage;

    @Entity.Column(value = "status", remark = "文章状态: draft(0草稿), published(1已发布), private(2私密)")
    private int status;

    @Entity.Column(value = "category_id", remark = "文章分类ID")
    private Long categoryId;

    @Entity.Column(value = "author_id", remark = "文章作者ID")
    private Long authorId;

    @Entity.Column(value = "is_top", remark = "是否置顶: 0否, 1是")
    private int top;

    @Entity.Column(value = "is_recommend", remark = "是否推荐: 0否, 1是")
    private int recommend;

    @Entity.Column(value = "is_commentable", remark = "是否允许评论: 0不允许, 1允许")
    private int allowComment;

    @Entity.Column(value = "publish_at", remark = "需要定时发布时设置")
    private LocalDateTime publishAt;

    @Entity.Column(value = "published_time", remark = "文章发布时间 (当状态变为 published(1) 时设置)")
    private LocalDateTime publishedTime;

    @Entity.Column(value = "modified_time", remark = "文章内容修改时间(当状态为 published(1) 时, 每次修改时设置)")
    private LocalDateTime modifiedTime;

    @Entity.Column(value = "view_count", remark = "文章浏览次数(每次浏览时设置)")
    private int viewCount;

    @Entity.Column(value = "like_count", remark = "文章点赞次数(每次点赞时设置)")
    private int likeCount;

    @Entity.Column(value = "word_count", remark = "文章字数")
    private int wordCount;

    @Entity.Column(value = "read_time", remark = "预计阅读时间,如：少于1分钟，3分钟，多于30分钟等")
    private String readTime;

    public Article() {
        this.allowComment = 1;
    }

    public Article(String title) {
        this();
        this.title = title;
    }
}
