package com.zqqiliyc.region;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.zqqiliyc.domain.entity.SysRegion;
import com.zqqiliyc.service.IRegionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author qili
 * @date 2025-09-07
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class TreeBuildTest {
    @Autowired
    private IRegionService regionService;

    private static final StopWatch timer = new StopWatch("region tree build");

    @Test
    public void build() {
        timer.start("query all region");
        List<SysRegion> regions = getAllRegions();
        timer.stop();

        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("code");
        treeNodeConfig.setParentIdKey("pcode");
        treeNodeConfig.setNameKey("name");
        treeNodeConfig.setDeep(5);

        timer.start("build tree");
        List<Tree<String>> treeList = TreeUtil.build(regions, null, treeNodeConfig, (region, treeNode) -> {
            treeNode.setId(region.getCode());
            treeNode.setName(region.getName());
            treeNode.setParentId(region.getPcode());
            treeNode.setWeight(region.getCode());
            treeNode.putExtra("level", region.getLevel());
        });
        timer.stop();

        System.out.println(timer.prettyPrint(TimeUnit.MILLISECONDS));

        Assertions.assertEquals(31, treeList.size());
    }

    private List<SysRegion> getAllRegions() {
        List<SysRegion> provinces = regionService.findProvinces();
        List<SysRegion> cities = regionService.findCities();
        List<SysRegion> districts = regionService.findDistricts();
        List<SysRegion> streets = regionService.findStreets();
        List<SysRegion> villages = regionService.findVillages();
        List<SysRegion> regions = new ArrayList<>(provinces);
        regions.addAll(cities);
        regions.addAll(districts);
        regions.addAll(streets);
        regions.addAll(villages);
        return regions;
    }
}
