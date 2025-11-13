package com.zqqiliyc.biz.core.entity;

import io.mybatis.provider.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-11-13
 */
@Getter
@Setter
@Entity.Table("rel_article_tag")
public class RelArticleTag extends BaseEntity {
    @Entity.Column(value = "article_id", remark = "文章ID")
    private Long articleId;

    @Entity.Column(value = "tag_id", remark = "标签ID")
    private Long tagId;
}
