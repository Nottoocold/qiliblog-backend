package com.zqqiliyc.service;

import com.zqqiliyc.domain.entity.SysRegion;

import java.util.List;
import java.util.Optional;

/**
 * @author qili
 * @date 2025-07-26
 */
public interface IRegionService {

    /**
     * 查询所有省
     * @return 省份列表
     */
    List<SysRegion> findProvinces();

    /**
     * 查询所有市
     * @return 市列表
     */
    List<SysRegion> findCities();

    /**
     * 查询所有市
     * @param provinceCode 省编码
     * @return 市列表
     */
    List<SysRegion> findCities(String provinceCode);

    /**
     * 查询所有区/县级行政级别
     * @return 区/县列表
     */
    List<SysRegion> findDistricts();

    /**
     * 查询所有区/县级行政级别
     * @param cityCode 市编码
     * @return 区/县列表
     */
    List<SysRegion> findDistricts(String cityCode);

    /**
     * 查询所有街道
     * @return 街道列表
     */
    List<SysRegion> findStreets();

    /**
     * 查询所有街道
     * @param districtCode 区/县编码
     * @return 街道列表
     */
    List<SysRegion> findStreets(String districtCode);

    /**
     * 查询所有村
     * @return 村列表
     */
    List<SysRegion> findVillages();

    /**
     * 查询所有村
     * @param streetCode 街道编码
     * @return 村列表
     */
    List<SysRegion> findVillages(String streetCode);

    /**
     * 根据编码查询
     * @param code 编码
     * @return 地区
     */
    Optional<SysRegion> findRegion(String code);
}
