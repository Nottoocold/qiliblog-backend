package com.zqqiliyc.ppublic.controller;

import com.zqqiliyc.biz.core.dto.article.ArticleQueryDTO;
import com.zqqiliyc.biz.core.entity.Article;
import com.zqqiliyc.biz.core.service.IArticleService;
import com.zqqiliyc.biz.core.vo.ArticleVo;
import com.zqqiliyc.biz.core.vo.transfer.ArticleVoConvertor;
import com.zqqiliyc.framework.web.bean.PageResult;
import com.zqqiliyc.framework.web.constant.WebApiConstants;
import com.zqqiliyc.framework.web.enums.GlobalErrorDict;
import com.zqqiliyc.framework.web.http.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公开文章接口（前台展示）
 *
 * @author qili
 * @date 2025-11-16
 */
@Tag(name = "公开文章接口")
@RestController
@RequiredArgsConstructor
@RequestMapping(WebApiConstants.API_PUBLIC_PREFIX + "/article")
public class PubArticleController {
    private final IArticleService articleService;
    private final ArticleVoConvertor articleVoConvertor;

    @Operation(summary = "分页查询已发布文章")
    @GetMapping("/page")
    public ApiResult<PageResult<ArticleVo>> pageQuery(ArticleQueryDTO queryDto) {
        // 只查询已发布的文章
        // TODO: 在 ArticleQueryDTO 中设置 status = 1
        PageResult<Article> pageInfo = articleService.findPageInfo(queryDto);
        PageResult<ArticleVo> result = pageInfo.map(articleVoConvertor::toViewVoList);
        return ApiResult.success(result);
    }

    @Operation(summary = "根据ID查询文章详情",
            parameters = @Parameter(name = "id", description = "文章ID", in = ParameterIn.PATH, required = true))
    @GetMapping("/{id}")
    public ApiResult<ArticleVo> getArticleById(@PathVariable Long id) {
        Article article = articleService.findById(id);
        if (article == null) {
            return ApiResult.error(GlobalErrorDict.PARAM_ERROR.getCode(), "文章不存在");
        }
        // 只返回已发布的文章
        if (article.getStatus() != 1) {
            return ApiResult.error(GlobalErrorDict.PARAM_ERROR.getCode(), "文章未发布");
        }
        return ApiResult.success(articleVoConvertor.toViewVo(article));
    }

    @Operation(summary = "根据slug查询文章详情",
            parameters = @Parameter(name = "slug", description = "文章URL友好标识符", in = ParameterIn.PATH, required = true))
    @GetMapping("/slug/{slug}")
    public ApiResult<ArticleVo> getArticleBySlug(@PathVariable String slug) {
        Article article = articleService.findBySlug(slug);
        if (article == null) {
            return ApiResult.error(GlobalErrorDict.PARAM_ERROR.getCode(), "文章不存在");
        }
        // 只返回已发布的文章
        if (article.getStatus() != 1) {
            return ApiResult.error(GlobalErrorDict.PARAM_ERROR.getCode(), "文章未发布");
        }
        return ApiResult.success(articleVoConvertor.toViewVo(article));
    }
}
