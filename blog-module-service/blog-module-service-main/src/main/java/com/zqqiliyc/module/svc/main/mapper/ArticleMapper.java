package com.zqqiliyc.module.svc.main.mapper;

import com.zqqiliyc.framework.web.mapper.Dao;
import com.zqqiliyc.module.svc.main.domain.entity.Article;
import io.mybatis.mapper.BaseMapper;

/**
 * @author qili
 * @date 2025-06-02
 */
public interface ArticleMapper extends BaseMapper<Article, Long>, Dao {
}
