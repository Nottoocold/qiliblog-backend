package com.zqqiliyc.ppublic.controller;

import cn.hutool.core.lang.tree.Tree;
import com.zqqiliyc.domain.entity.SysRegion;
import com.zqqiliyc.framework.web.constant.WebApiConstants;
import com.zqqiliyc.framework.web.http.ApiResult;
import com.zqqiliyc.service.IRegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author qili
 * @date 2025-09-07
 */
@Tag(name = "系统区域接口")
@RestController
@RequiredArgsConstructor
@RequestMapping(WebApiConstants.API_PUBLIC_PREFIX)
public class SysRegionController {
    private final IRegionService sysRegionService;

    @Operation(summary = "获取区域树",
            parameters = @Parameter(name = "level", description = "区域等级:取值范围1-3", in = ParameterIn.PATH, required = true))
    @GetMapping("/region/tree/{level}")
    public ApiResult<List<Tree<String>>> findRegionTree(@PathVariable int level) {
        return ApiResult.success(sysRegionService.findRegionTree(level));
    }

    @Operation(summary = "获取所有省")
    @GetMapping("/region/provinces")
    public ApiResult<List<SysRegion>> findProvinces() {
        return ApiResult.success(sysRegionService.findProvinces());
    }

    @Operation(summary = "获取所有市")
    @GetMapping("/region/cities")
    public ApiResult<List<SysRegion>> findCities() {
        return ApiResult.success(sysRegionService.findCities());
    }

    @Operation(summary = "获取所有区")
    @GetMapping("/region/districts")
    public ApiResult<List<SysRegion>> findDistricts() {
        return ApiResult.success(sysRegionService.findDistricts());
    }

    @Operation(summary = "获取所有街道")
    @GetMapping("/region/streets")
    public ApiResult<List<SysRegion>> findStreets() {
        return ApiResult.success(sysRegionService.findStreets());
    }

    @Operation(summary = "获取所有村")
    @GetMapping("/region/villages")
    public ApiResult<List<SysRegion>> findVillages() {
        return ApiResult.success(sysRegionService.findVillages());
    }

    @Operation(summary = "获取指定省下的所有市",
            parameters = @Parameter(name = "provinceCode", description = "省编码", in = ParameterIn.PATH, required = true))
    @GetMapping("/region/{provinceCode}/cities")
    public ApiResult<List<SysRegion>> findCities(@PathVariable String provinceCode) {
        return ApiResult.success(sysRegionService.findCities(provinceCode));
    }

    @Operation(summary = "获取指定市下的所有区",
            parameters = @Parameter(name = "cityCode", description = "市编码", in = ParameterIn.PATH, required = true))
    @GetMapping("/region/{cityCode}/districts")
    public ApiResult<List<SysRegion>> findDistricts(@PathVariable String cityCode) {
        return ApiResult.success(sysRegionService.findDistricts(cityCode));
    }

    @Operation(summary = "获取指定区下的所有街道",
            parameters = @Parameter(name = "districtCode", description = "区编码", in = ParameterIn.PATH, required = true))
    @GetMapping("/region/{districtCode}/streets")
    public ApiResult<List<SysRegion>> findStreets(@PathVariable String districtCode) {
        return ApiResult.success(sysRegionService.findStreets(districtCode));
    }

    @Operation(summary = "获取指定街道下的所有村",
            parameters = @Parameter(name = "streetCode", description = "街道编码", in = ParameterIn.PATH, required = true))
    @GetMapping("/region/{streetCode}/villages")
    public ApiResult<List<SysRegion>> findVillages(@PathVariable String streetCode) {
        return ApiResult.success(sysRegionService.findVillages(streetCode));
    }
}
