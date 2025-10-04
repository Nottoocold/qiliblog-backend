package com.zqqiliyc.biz.core.service;

import com.zqqiliyc.biz.core.entity.SysRegion;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author qili
 * @date 2025-07-26
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class SysRegionServiceTest {
    @Autowired
    private IRegionService regionService;

    @Test
    @Order(0)
    void testFindProvinces() {
        List<SysRegion> provinces = regionService.findProvinces();
        assertEquals(31, provinces.size());
    }

    @Test
    @Order(1)
    void testFindCities() {
        List<SysRegion> cities = regionService.findCities();
        assertEquals(342, cities.size());
    }

    @Test
    @Order(2)
    void testFindDistricts() {
        List<SysRegion> districts = regionService.findDistricts();
        assertEquals(2978, districts.size());
    }

    @Test
    @Order(3)
    void testFindStreets() {
        List<SysRegion> streets = regionService.findStreets();
        assertEquals(41352, streets.size());
    }

    @Test
    @Order(4)
    void testFindVillages() {
        List<SysRegion> villages = regionService.findVillages();
        assertEquals(620573, villages.size());
    }
}
