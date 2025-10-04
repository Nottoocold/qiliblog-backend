package com.zqqiliyc.biz.core.service.impl;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.zqqiliyc.biz.core.entity.SysRegion;
import com.zqqiliyc.biz.core.repository.mapper.SysRegionMapper;
import com.zqqiliyc.biz.core.service.IRegionService;
import com.zqqiliyc.framework.web.spring.SpringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class SysRegionService implements IRegionService {
    private final SysRegionMapper regionMapper;

    /**
     * 查询地区树
     *
     * @param level 需要的深度-1省，2省、市，3省、市、区/县，4省、市、区/县、街道，5省、市、区/县、街道、村
     * @return 地区树
     */
    @Override
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
    public List<SysRegion> findProvinces() {
        return regionMapper.selectProvinces();
    }

    @Override
    public List<SysRegion> findCities() {
        return regionMapper.selectCities();
    }

    @Override
    public List<SysRegion> findDistricts() {
        return regionMapper.selectDistricts();
    }

    @Override
    public List<SysRegion> findStreets() {
        return Collections.emptyList();
    }

    @Override
    public List<SysRegion> findVillages() {
        return Collections.emptyList();
    }

    @Override
    public List<SysRegion> findCities(String provinceCode) {
        return regionMapper.selectCitiesByProvinceCode(provinceCode);
    }

    @Override
    public List<SysRegion> findDistricts(String cityCode) {
        return regionMapper.selectDistrictsByCityCode(cityCode);
    }

    @Override
    public List<SysRegion> findStreets(String districtCode) {
        return regionMapper.selectStreetsByDistrictCode(districtCode);
    }

    @Override
    public List<SysRegion> findVillages(String streetCode) {
        return regionMapper.selectVillagesByStreetCode(streetCode);
    }

    @Override
    public Optional<SysRegion> findRegion(String code) {
        return Optional.ofNullable(regionMapper.selectByCode(code));
    }
}
