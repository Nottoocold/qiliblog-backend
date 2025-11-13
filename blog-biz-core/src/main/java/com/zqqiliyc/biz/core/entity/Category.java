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
@Entity.Table("blog_category")
public class Category extends BaseEntity {
    @Entity.Column(value = "name", remark = "分类名称 (必须唯一，如 技术, 生活)")
    private String name;

    @Entity.Column(value = "slug", remark = "分类 URL 友好标识符 (必须唯一，如 tech, life)")
    private String slug;

    @Entity.Column(value = "post_count", remark = "分类下的文章数量")
    private int postCount;

    @Entity.Column(value = "description", remark = "分类描述")
    private String description;
}
