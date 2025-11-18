package com.zqqiliyc.biz.core.vo.transfer;

import com.zqqiliyc.biz.core.dto.ViewVoConvertor;
import com.zqqiliyc.biz.core.entity.Article;
import com.zqqiliyc.biz.core.entity.SysUser;
import com.zqqiliyc.biz.core.service.IRelArticleTagService;
import com.zqqiliyc.biz.core.service.ISysUserService;
import com.zqqiliyc.biz.core.vo.ArticleVo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author qili
 * @date 2025-09-30
 */
@Component
public class ArticleVoConvertor implements ViewVoConvertor<Article, ArticleVo> {
    private final IRelArticleTagService relArticleTagService;
    private final ISysUserService sysUserService;

    public ArticleVoConvertor(IRelArticleTagService relArticleTagService,
                              ISysUserService sysUserService) {
        this.relArticleTagService = relArticleTagService;
        this.sysUserService = sysUserService;
    }

    @Override
    public void customize(List<ArticleVo> targetList, List<Article> sourceList) {
        Map<Long, Set<Long>> postTagIdMap = new HashMap<>(sourceList.size());
        sourceList.forEach(article -> postTagIdMap.put(article.getId(), relArticleTagService.findByArticleId(article.getId())));

        Map<Long, SysUser> userMap = sysUserService.findByFieldList(SysUser::getId, sourceList.stream().map(Article::getAuthorId).collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(SysUser::getId, Function.identity()));

        for (ArticleVo vo : targetList) {
            vo.setAuthorName(userMap.get(vo.getAuthorId()).getNickname());
            vo.setTagIds(postTagIdMap.get(vo.getId()));
        }
    }
}
