package com.zqqiliyc.service.impl;

import com.zqqiliyc.domain.entity.Article;
import com.zqqiliyc.mapper.ArticleMapper;
import com.zqqiliyc.service.IArticleService;
import com.zqqiliyc.service.base.AbstractExtendService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qili
 * @date 2025-06-02
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArticleService extends AbstractExtendService<Article, Long, ArticleMapper> implements IArticleService {
}
