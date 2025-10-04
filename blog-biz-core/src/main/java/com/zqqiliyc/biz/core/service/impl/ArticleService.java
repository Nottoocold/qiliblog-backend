package com.zqqiliyc.biz.core.service.impl;

import com.zqqiliyc.biz.core.entity.Article;
import com.zqqiliyc.biz.core.repository.mapper.ArticleMapper;
import com.zqqiliyc.biz.core.service.IArticleService;
import com.zqqiliyc.biz.core.service.base.AbstractBaseService;
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
