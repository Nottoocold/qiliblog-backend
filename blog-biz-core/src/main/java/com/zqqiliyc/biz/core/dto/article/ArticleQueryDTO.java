package com.zqqiliyc.biz.core.dto.article;

import com.zqqiliyc.biz.core.dto.AbstractQueryDTO;
import com.zqqiliyc.biz.core.entity.Article;
import io.mybatis.mapper.example.Example;
import lombok.Getter;
import lombok.Setter;

/**
 * @author qili
 * @date 2025-11-15
 */
@Getter
@Setter
public class ArticleQueryDTO extends AbstractQueryDTO<Article> {


    @Override
    protected void fillExample(Example<Article> example) {

    }
}
