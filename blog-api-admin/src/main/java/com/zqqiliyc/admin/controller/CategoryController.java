package com.zqqiliyc.admin.controller;

import com.zqqiliyc.domain.dto.cate.CategoryCreateDto;
import com.zqqiliyc.domain.dto.cate.CategoryQueryDto;
import com.zqqiliyc.domain.dto.cate.CategoryUpdateDto;
import com.zqqiliyc.domain.entity.Category;
import com.zqqiliyc.domain.vo.CategoryVo;
import com.zqqiliyc.domain.vo.transfer.CategoryVoTransfer;
import com.zqqiliyc.framework.web.bean.PageResult;
import com.zqqiliyc.framework.web.constant.WebApiConstants;
import com.zqqiliyc.framework.web.http.ApiResult;
import com.zqqiliyc.service.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author qili
 * @date 2025-09-30
 */
@Tag(name = "文章分类接口")
@Slf4j
@RestController
@RequestMapping(WebApiConstants.API_ADMIN_PREFIX + "/category")
@RequiredArgsConstructor
class CategoryController {
    private final ICategoryService categoryService;
    private final CategoryVoTransfer categoryVoTransfer;

    @Operation(summary = "分页查询分类")
    @GetMapping("/page")
    public ApiResult<PageResult<CategoryVo>> pageQuery(CategoryQueryDto queryDto) {
        PageResult<Category> pageInfo = categoryService.findPageInfo(queryDto);
        PageResult<CategoryVo> result = pageInfo.map(categoryVoTransfer::toViewVoList);
        return ApiResult.success(result);
    }

    @Operation(summary = "查询分类")
    @GetMapping("/{id}")
    public ApiResult<CategoryVo> query(@PathVariable Long id) {
        CategoryVo CategoryVo = categoryVoTransfer.toViewVo(categoryService.findById(id));
        return ApiResult.success(CategoryVo);
    }

    @Operation(summary = "创建分类")
    @PostMapping
    public ApiResult<CategoryVo> create(@RequestBody @Validated CategoryCreateDto dto) {
        CategoryVo CategoryVo = categoryVoTransfer.toViewVo(categoryService.create(dto));
        return ApiResult.success(CategoryVo);
    }

    @Operation(summary = "更新分类")
    @PutMapping
    public ApiResult<CategoryVo> update(@RequestBody @Validated CategoryUpdateDto dto) {
        CategoryVo CategoryVo = categoryVoTransfer.toViewVo(categoryService.update(dto));
        return ApiResult.success(CategoryVo);
    }

    @Operation(summary = "删除分类")
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ApiResult.success();
    }
}
