package com.zqqiliyc.biz.core.entity;

import io.mybatis.provider.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-06-01
 */
@Getter
@Setter
@Entity.Table("blog_tags")
public class Tag extends BaseEntity {
    @Entity.Column(value = "name", remark = "标签名")
    private String name;

    @Entity.Column(value = "slug", remark = "标签 URL 友好标识符 (必须唯一，如 java, spring-boot)")
    private String slug;

    @Entity.Column(value = "post_count", remark = "标签下文章数")
    private int postCount;

    @Entity.Column(value = "description", remark = "标签描述")
    private String description;
}
