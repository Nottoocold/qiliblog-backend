package com.zqqiliyc.service.impl;

import com.zqqiliyc.domain.entity.Article;
import com.zqqiliyc.mapper.ArticleMapper;
import com.zqqiliyc.service.IArticleService;
import io.mybatis.service.AbstractService;
import org.springframework.stereotype.Service;

/**
 * @author qili
 * @date 2025-06-02
 */
@Service
public class ArticleService extends AbstractService<Article, Long, ArticleMapper> implements IArticleService {
}
