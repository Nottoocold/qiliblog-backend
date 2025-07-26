package com.zqqiliyc.service.impl;

import com.zqqiliyc.domain.entity.SysRegion;
import com.zqqiliyc.repository.mapper.SysRegionMapper;
import com.zqqiliyc.service.IRegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @author qili
 * @date 2025-07-26
 */
@Service
@RequiredArgsConstructor
public class SysRegionService implements IRegionService {
    private final SysRegionMapper regionMapper;

    @Override
    public List<SysRegion> findProvinces() {
        return regionMapper.selectProvinces();
    }

    @Override
    public List<SysRegion> findCities() {
        return regionMapper.selectCities();
    }

    @Override
    public List<SysRegion> findCities(String provinceCode) {
        return regionMapper.selectCitiesByProvinceCode(provinceCode);
    }

    @Override
    public List<SysRegion> findDistricts() {
        return regionMapper.selectDistricts();
    }

    @Override
    public List<SysRegion> findDistricts(String cityCode) {
        return regionMapper.selectDistrictsByCityCode(cityCode);
    }

    @Override
    public List<SysRegion> findStreets() {
        return regionMapper.selectStreets();
    }

    @Override
    public List<SysRegion> findStreets(String districtCode) {
        return regionMapper.selectStreetsByDistrictCode(districtCode);
    }

    @Override
    public List<SysRegion> findVillages() {
        return regionMapper.selectVillages();
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
