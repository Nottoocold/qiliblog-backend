package com.zqqiliyc.admin.controller;

import com.zqqiliyc.biz.core.dto.tag.TagCreateDTO;
import com.zqqiliyc.biz.core.dto.tag.TagQueryDTO;
import com.zqqiliyc.biz.core.dto.tag.TagUpdateDTO;
import com.zqqiliyc.biz.core.entity.Tag;
import com.zqqiliyc.biz.core.service.ITagService;
import com.zqqiliyc.biz.core.vo.TagVo;
import com.zqqiliyc.biz.core.vo.transfer.TagVoConvertor;
import com.zqqiliyc.framework.web.bean.PageResult;
import com.zqqiliyc.framework.web.constant.WebApiConstants;
import com.zqqiliyc.framework.web.http.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qili
 * @date 2025-09-30
 */
@io.swagger.v3.oas.annotations.tags.Tag(name = "标签接口")
@Slf4j
@RestController
@RequestMapping(WebApiConstants.API_ADMIN_PREFIX + "/tag")
@RequiredArgsConstructor
class TagController {
    private final ITagService tagService;
    private final TagVoConvertor tagVoTransfer;

    @Operation(summary = "分页查询标签")
    @GetMapping("/page")
    public ApiResult<PageResult<TagVo>> pageQuery(TagQueryDTO queryDto) {
        PageResult<Tag> pageInfo = tagService.findPageInfo(queryDto);
        PageResult<TagVo> result = pageInfo.map(tagVoTransfer::toViewVoList);
        return ApiResult.success(result);
    }

    @Operation(summary = "列表查询标签")
    @GetMapping("/list")
    public ApiResult<List<TagVo>> listQuery(TagQueryDTO queryDto) {
        List<Tag> categoryList = tagService.findList(queryDto);
        List<TagVo> result = tagVoTransfer.toViewVoList(categoryList);
        return ApiResult.success(result);
    }

    @Operation(summary = "查询标签")
    @GetMapping("/{id}")
    public ApiResult<TagVo> query(@PathVariable Long id) {
        TagVo tagVo = tagVoTransfer.toViewVo(tagService.findById(id));
        return ApiResult.success(tagVo);
    }

    @Operation(summary = "创建标签")
    @PostMapping
    public ApiResult<TagVo> create(@RequestBody @Validated TagCreateDTO dto) {
        TagVo tagVo = tagVoTransfer.toViewVo(tagService.create(dto));
        return ApiResult.success(tagVo);
    }

    @Operation(summary = "更新标签")
    @PutMapping
    public ApiResult<TagVo> update(@RequestBody @Validated TagUpdateDTO dto) {
        TagVo tagVo = tagVoTransfer.toViewVo(tagService.update(dto));
        return ApiResult.success(tagVo);
    }

    @Operation(summary = "删除标签")
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        tagService.deleteById(id);
        return ApiResult.success();
    }
}
