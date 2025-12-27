package com.zqqiliyc.module.svc.main.domain.vo.transfer;

import com.zqqiliyc.framework.web.domain.vo.ViewVoConvertor;
import com.zqqiliyc.module.svc.main.domain.entity.Article;
import com.zqqiliyc.module.svc.main.domain.vo.ArticleVo;
import com.zqqiliyc.module.svc.main.service.IRelArticleTagService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author qili
 * @date 2025-09-30
 */
@Component
public class ArticleVoConvertor implements ViewVoConvertor<Article, ArticleVo> {
    private final IRelArticleTagService relArticleTagService;

    public ArticleVoConvertor(IRelArticleTagService relArticleTagService) {
        this.relArticleTagService = relArticleTagService;
    }

    @Override
    public void customize(List<ArticleVo> targetList, List<Article> sourceList) {
        Map<Long, Set<Long>> postTagIdMap = new HashMap<>(sourceList.size());
        sourceList.forEach(article -> postTagIdMap.put(article.getId(), relArticleTagService.findByArticleId(article.getId())));

        for (ArticleVo vo : targetList) {
            vo.setTagIds(postTagIdMap.get(vo.getId()));
        }
    }
}
