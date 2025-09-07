package com.zqqiliyc.ppublic.controller;

import cn.hutool.core.lang.tree.Tree;
import com.zqqiliyc.domain.entity.SysRegion;
import com.zqqiliyc.framework.web.constant.WebApiConstants;
import com.zqqiliyc.framework.web.http.ApiResult;
import com.zqqiliyc.service.impl.SysRegionService;
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
@RestController
@RequiredArgsConstructor
@RequestMapping(WebApiConstants.API_PUBLIC_PREFIX)
public class SysRegionController {
    private final SysRegionService sysRegionService;

    @GetMapping("/region/tree/{level}")
    public ApiResult<List<Tree<String>>> findRegionTree(@PathVariable int level) {
        return ApiResult.success(sysRegionService.findRegionTree(level));
    }

    @GetMapping("/region/provinces")
    public ApiResult<List<SysRegion>> findProvinces() {
        return ApiResult.success(sysRegionService.findProvinces());
    }

    @GetMapping("/region/cities")
    public ApiResult<List<SysRegion>> findCities() {
        return ApiResult.success(sysRegionService.findCities());
    }

    @GetMapping("/region/districts")
    public ApiResult<List<SysRegion>> findDistricts() {
        return ApiResult.success(sysRegionService.findDistricts());
    }

    @GetMapping("/region/streets")
    public ApiResult<List<SysRegion>> findStreets() {
        return ApiResult.success(sysRegionService.findStreets());
    }

    @GetMapping("/region/villages")
    public ApiResult<List<SysRegion>> findVillages() {
        return ApiResult.success(sysRegionService.findVillages());
    }

    @GetMapping("/region/{provinceCode}/cities")
    public ApiResult<List<SysRegion>> findCities(@PathVariable String provinceCode) {
        return ApiResult.success(sysRegionService.findCities(provinceCode));
    }

    @GetMapping("/region/{cityCode}/districts")
    public ApiResult<List<SysRegion>> findDistricts(@PathVariable String cityCode) {
        return ApiResult.success(sysRegionService.findDistricts(cityCode));
    }

    @GetMapping("/region/{districtCode}/streets")
    public ApiResult<List<SysRegion>> findStreets(@PathVariable String districtCode) {
        return ApiResult.success(sysRegionService.findStreets(districtCode));
    }

    @GetMapping("/region/{streetCode}/villages")
    public ApiResult<List<SysRegion>> findVillages(@PathVariable String streetCode) {
        return ApiResult.success(sysRegionService.findVillages(streetCode));
    }
}
