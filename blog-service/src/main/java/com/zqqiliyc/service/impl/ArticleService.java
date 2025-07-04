package com.zqqiliyc.service.impl;

import com.zqqiliyc.domain.entity.Article;
import com.zqqiliyc.repository.mapper.ArticleMapper;
import com.zqqiliyc.service.IArticleService;
import com.zqqiliyc.service.base.AbstractBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qili
 * @date 2025-06-02
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArticleService extends AbstractBaseService<Article, Long, ArticleMapper> implements IArticleService {
}
