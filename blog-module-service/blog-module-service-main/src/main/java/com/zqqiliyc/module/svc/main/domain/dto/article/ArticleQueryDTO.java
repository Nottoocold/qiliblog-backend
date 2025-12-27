package com.zqqiliyc.module.svc.main.domain.dto.article;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.framework.web.domain.dto.AbstractQueryDTO;
import com.zqqiliyc.module.svc.main.domain.entity.Article;
import io.mybatis.mapper.example.Example;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author qili
 * @date 2025-11-15
 */
@Getter
@Setter
public class ArticleQueryDTO extends AbstractQueryDTO<Article> {
    @Schema(description = "文章ID列表")
    private List<Long> ids;

    @Schema(description = "关键字,title,summary")
    private String keyword;

    @Schema(description = "文章状态: draft(0草稿), published(1已发布), private(2私密)")
    private Integer status;

    @Schema(description = "文章分类ID")
    private Long categoryId;

    @Schema(description = "文章标签ID")
    private Long tagId;

    @Override
    protected void fillExample(Example<Article> example) {
        Example.Criteria<Article> criteria = example.createCriteria();
        if (StrUtil.isNotBlank(keyword)) {
            criteria.andOr(List.of(
                    example.orPart().like(Article::getTitle, "%" + keyword + "%"),
                    example.orPart().like(Article::getSummary, "%" + keyword + "%")
            ));
        }

        criteria.andIn(CollUtil.isNotEmpty(ids), Article::getId, ids)
                .andEqualTo(status != null, Article::getStatus, status)
                .andEqualTo(categoryId != null, Article::getCategoryId, categoryId)
                .andCondition(tagId != null, "id in (select article_id from rel_article_tag where tag_id = %s)".formatted(tagId));
    }
}
