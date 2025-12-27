package com.zqqiliyc.module.api.admin.controller;

import com.zqqiliyc.framework.web.bean.PageResult;
import com.zqqiliyc.framework.web.constant.WebApiConstants;
import com.zqqiliyc.framework.web.http.ApiResult;
import com.zqqiliyc.module.svc.main.domain.dto.cate.CategoryCreateDTO;
import com.zqqiliyc.module.svc.main.domain.dto.cate.CategoryQueryDTO;
import com.zqqiliyc.module.svc.main.domain.dto.cate.CategoryUpdateDTO;
import com.zqqiliyc.module.svc.main.domain.entity.Category;
import com.zqqiliyc.module.svc.main.domain.vo.CategoryVo;
import com.zqqiliyc.module.svc.main.domain.vo.transfer.CategoryVoConvertor;
import com.zqqiliyc.module.svc.main.service.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private final CategoryVoConvertor categoryVoTransfer;

    @Operation(summary = "分页查询分类")
    @GetMapping("/page")
    public ApiResult<PageResult<CategoryVo>> pageQuery(CategoryQueryDTO queryDto) {
        PageResult<Category> pageInfo = categoryService.findPageInfo(queryDto);
        PageResult<CategoryVo> result = pageInfo.map(categoryVoTransfer::toViewVoList);
        return ApiResult.success(result);
    }

    @Operation(summary = "列表查询分类")
    @GetMapping("list")
    public ApiResult<List<CategoryVo>> listQuery(CategoryQueryDTO queryDto) {
        List<Category> categoryList = categoryService.findList(queryDto);
        List<CategoryVo> result = categoryVoTransfer.toViewVoList(categoryList);
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
    public ApiResult<CategoryVo> create(@RequestBody @Validated CategoryCreateDTO dto) {
        CategoryVo CategoryVo = categoryVoTransfer.toViewVo(categoryService.create(dto));
        return ApiResult.success(CategoryVo);
    }

    @Operation(summary = "更新分类")
    @PutMapping
    public ApiResult<CategoryVo> update(@RequestBody @Validated CategoryUpdateDTO dto) {
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
