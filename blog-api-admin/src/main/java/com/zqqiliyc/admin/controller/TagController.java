package com.zqqiliyc.admin.controller;

import com.zqqiliyc.biz.core.dto.tag.TagCreateDto;
import com.zqqiliyc.biz.core.dto.tag.TagQueryDto;
import com.zqqiliyc.biz.core.dto.tag.TagUpdateDto;
import com.zqqiliyc.biz.core.entity.Tag;
import com.zqqiliyc.biz.core.service.ITagService;
import com.zqqiliyc.biz.core.vo.TagVo;
import com.zqqiliyc.biz.core.vo.transfer.TagVoTransfer;
import com.zqqiliyc.framework.web.bean.PageResult;
import com.zqqiliyc.framework.web.constant.WebApiConstants;
import com.zqqiliyc.framework.web.http.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    private final TagVoTransfer tagVoTransfer;

    @Operation(summary = "分页查询标签")
    @GetMapping("/page")
    public ApiResult<PageResult<TagVo>> pageQuery(TagQueryDto queryDto) {
        PageResult<Tag> pageInfo = tagService.findPageInfo(queryDto);
        PageResult<TagVo> result = pageInfo.map(tagVoTransfer::toViewVoList);
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
    public ApiResult<TagVo> create(@RequestBody @Validated TagCreateDto dto) {
        TagVo tagVo = tagVoTransfer.toViewVo(tagService.create(dto));
        return ApiResult.success(tagVo);
    }

    @Operation(summary = "更新标签")
    @PutMapping
    public ApiResult<TagVo> update(@RequestBody @Validated TagUpdateDto dto) {
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
