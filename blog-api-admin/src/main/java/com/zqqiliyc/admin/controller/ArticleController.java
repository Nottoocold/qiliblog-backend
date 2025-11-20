package com.zqqiliyc.admin.controller;

import com.zqqiliyc.biz.core.dto.article.ArticleDraftCreateDTO;
import com.zqqiliyc.biz.core.dto.article.ArticleQueryDTO;
import com.zqqiliyc.biz.core.dto.article.ArticleUpdateDTO;
import com.zqqiliyc.biz.core.entity.Article;
import com.zqqiliyc.biz.core.service.IArticleService;
import com.zqqiliyc.biz.core.vo.ArticleVo;
import com.zqqiliyc.biz.core.vo.transfer.ArticleVoConvertor;
import com.zqqiliyc.framework.web.bean.PageResult;
import com.zqqiliyc.framework.web.constant.WebApiConstants;
import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import com.zqqiliyc.framework.web.http.ApiResult;
import com.zqqiliyc.framework.web.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qili
 * @date 2025-11-15
 */
@Tag(name = "文章接口")
@Slf4j
@RestController
@RequestMapping(WebApiConstants.API_ADMIN_PREFIX + "/post")
@RequiredArgsConstructor
public class ArticleController {
    private final IArticleService articleService;
    private final ArticleVoConvertor articleVoConvertor;

    @Operation(summary = "分页查询文章")
    @GetMapping("/page")
    public ApiResult<PageResult<ArticleVo>> pageQuery(ArticleQueryDTO queryDto) {
        PageResult<Article> pageInfo = articleService.findPageInfo(queryDto);
        PageResult<ArticleVo> result = pageInfo.map(articleVoConvertor::toViewVoList);
        return ApiResult.success(result);
    }

    @Operation(summary = "列表查询文章")
    @GetMapping("/list")
    public ApiResult<List<ArticleVo>> listQuery(ArticleQueryDTO queryDto) {
        List<Article> articleList = articleService.findList(queryDto);
        return ApiResult.success(articleVoConvertor.toViewVoList(articleList));
    }

    @Operation(summary = "根据ID查询文章详情")
    @GetMapping("/{id}")
    public ApiResult<ArticleVo> getArticleById(@PathVariable Long id) {
        Article article = articleService.findById(id);
        if (article == null) {
            return ApiResult.error(GlobalErrorDict.PARAM_ERROR.getCode(), "文章不存在");
        }
        return ApiResult.success(articleVoConvertor.toViewVo(article));
    }

    @Operation(summary = "根据slug查询文章详情")
    @GetMapping("/slug/{slug}")
    public ApiResult<ArticleVo> getArticleBySlug(@PathVariable String slug) {
        Article article = articleService.findBySlug(slug);
        if (article == null) {
            return ApiResult.error(GlobalErrorDict.PARAM_ERROR.getCode(), "文章不存在");
        }
        return ApiResult.success(articleVoConvertor.toViewVo(article));
    }

    @Operation(summary = "保存草稿文章")
    @PostMapping("/draft")
    public ApiResult<ArticleVo> saveDraft(@RequestBody @Validated ArticleDraftCreateDTO dto) {
        dto.setAuthorId(SecurityUtils.getCurrentUserId());
        return ApiResult.success(articleVoConvertor.toViewVo(articleService.createDraft(dto)));
    }

    @Operation(summary = "更新文章")
    @PutMapping
    public ApiResult<ArticleVo> updateArticle(@RequestBody @Validated ArticleUpdateDTO dto) {
        Article article = articleService.updateArticle(dto);
        return ApiResult.success(articleVoConvertor.toViewVo(article));
    }

    @Operation(summary = "删除文章")
    @DeleteMapping("/{id}")
    public ApiResult<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteById(id);
        return ApiResult.success();
    }
}
