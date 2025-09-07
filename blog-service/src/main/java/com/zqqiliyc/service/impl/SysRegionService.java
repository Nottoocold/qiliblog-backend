package com.zqqiliyc.service.impl;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.zqqiliyc.domain.entity.SysRegion;
import com.zqqiliyc.framework.web.spring.SpringUtils;
import com.zqqiliyc.repository.mapper.SysRegionMapper;
import com.zqqiliyc.service.IRegionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author qili
 * @date 2025-07-26
 */
@Slf4j
@Service
@CacheConfig(cacheNames = SysRegionService.CACHE_NAME, cacheManager = "redisCacheManager")
@RequiredArgsConstructor
public class SysRegionService implements IRegionService {
    private final SysRegionMapper regionMapper;
    public static final String CACHE_NAME = "system:regions";

    /**
     * 查询地区树
     *
     * @param level 需要的深度-1省，2省、市，3省、市、区/县，4省、市、区/县、街道，5省、市、区/县、街道、村
     * @return 地区树
     */
    @Override
    @Cacheable(key = "'tree:' + #level", condition = "#level >= 1 && #level <= 3", unless = "#result.empty")
    public List<Tree<String>> findRegionTree(int level) {
        StopWatch timer = new StopWatch("region tree build");
        timer.start("query all region");
        List<SysRegion> regions = getAllRegions();
        timer.stop();
        if (log.isDebugEnabled()) {
            log.debug("query all region time: {}ms", timer.getLastTaskTimeMillis());
        }

        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("code");
        treeNodeConfig.setParentIdKey("pcode");
        treeNodeConfig.setNameKey("name");
        treeNodeConfig.setDeep(Math.max(Math.min(level, 3), 1));

        timer.start("build tree");
        List<Tree<String>> treeList = TreeUtil.build(regions, null, treeNodeConfig, (region, treeNode) -> {
            treeNode.setId(region.getCode());
            treeNode.setName(region.getName());
            treeNode.setParentId(region.getPcode());
            treeNode.setWeight(region.getCode());
            treeNode.putExtra("level", region.getLevel());
        });
        timer.stop();
        if (log.isDebugEnabled()) {
            log.debug("build tree time: {}ms", timer.getLastTaskTimeMillis());
        }

        if (log.isDebugEnabled()) {
            System.out.println(timer.prettyPrint(TimeUnit.MILLISECONDS));
        }

        return treeList;
    }

    private List<SysRegion> getAllRegions() {
        SysRegionService self = SpringUtils.getBean(this.getClass());
        List<SysRegion> provinces = self.findProvinces();
        List<SysRegion> cities = self.findCities();
        List<SysRegion> districts = self.findDistricts();
        List<SysRegion> streets = self.findStreets();
        List<SysRegion> villages = self.findVillages();
        List<SysRegion> regions = new ArrayList<>(provinces);
        regions.addAll(cities);
        regions.addAll(districts);
        regions.addAll(streets);
        regions.addAll(villages);
        return regions;
    }

    @Override
    @Cacheable(key = "'provinces:all'")
    public List<SysRegion> findProvinces() {
        return regionMapper.selectProvinces();
    }

    @Override
    @Cacheable(key = "'cities:all'")
    public List<SysRegion> findCities() {
        return regionMapper.selectCities();
    }

    @Override
    @Cacheable(key = "'cities:provinceCode:' + #provinceCode",
            condition = "#provinceCode != null && !#provinceCode.empty", unless = "#result.empty")
    public List<SysRegion> findCities(String provinceCode) {
        return regionMapper.selectCitiesByProvinceCode(provinceCode);
    }

    @Override
    @Cacheable(key = "'districts:all'")
    public List<SysRegion> findDistricts() {
        return regionMapper.selectDistricts();
    }

    @Override
    @Cacheable(key = "'districts:cityCode:' + #cityCode",
            condition = "#cityCode != null && !#cityCode.empty", unless = "#result.empty")
    public List<SysRegion> findDistricts(String cityCode) {
        return regionMapper.selectDistrictsByCityCode(cityCode);
    }

    @Override
    @Cacheable(key = "'streets:all'")
    public List<SysRegion> findStreets() {
        return regionMapper.selectStreets();
    }

    @Override
    @Cacheable(key = "'streets:districtCode:' + #districtCode",
            condition = "#districtCode != null && !#districtCode.empty", unless = "#result.empty")
    public List<SysRegion> findStreets(String districtCode) {
        return regionMapper.selectStreetsByDistrictCode(districtCode);
    }

    @Override
    @Cacheable(key = "'villages:all'", unless = "#result.empty")
    public List<SysRegion> findVillages() {
        return Collections.emptyList();
    }

    @Override
    @Cacheable(key = "'villages:streetCode:' + #streetCode",
            condition = "#streetCode != null && !#streetCode.empty", unless = "#result.empty")
    public List<SysRegion> findVillages(String streetCode) {
        return regionMapper.selectVillagesByStreetCode(streetCode);
    }

    @Override
    @Cacheable(key = "'single:code:' + #code",
            condition = "#code != null && !#code.empty", unless = "#result.empty")
    public Optional<SysRegion> findRegion(String code) {
        return Optional.ofNullable(regionMapper.selectByCode(code));
    }
}
