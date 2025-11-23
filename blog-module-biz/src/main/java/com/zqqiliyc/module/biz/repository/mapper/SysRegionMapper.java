package com.zqqiliyc.module.biz.repository.mapper;

import com.zqqiliyc.module.biz.entity.SysRegion;
import com.zqqiliyc.module.biz.repository.Dao;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;

/**
 * @author qili
 * @date 2025-07-26
 */
public interface SysRegionMapper extends Dao {

    @Insert("insert into sys_region (code, name, pcode, level) values (#{code}, #{name}, #{pcode}, #{level})")
    int insert(SysRegion sysRegion);

    @Insert("""
    <script>
        insert into sys_region (code, name, pcode, level) values 
        <foreach collection="list" item="item" separator=",">
        (#{item.code}, #{item.name}, #{item.pcode}, #{item.level})
        </foreach>
    </script>
    """)
    int batchInsert(@Param("list") Collection<SysRegion> regions);

    @Select("select * from sys_region where level = 1 order by code")
    List<SysRegion> selectProvinces();

    @Select("select * from sys_region where level = 2 order by code")
    List<SysRegion> selectCities();

    @Select("select * from sys_region where level = 2 and pcode = #{pc} order by code")
    List<SysRegion> selectCitiesByProvinceCode(@Param("pc") String provinceCode);

    @Select("select * from sys_region where level = 3 order by code")
    List<SysRegion> selectDistricts();

    @Select("select * from sys_region where level = 3 and pcode = #{cc} order by code")
    List<SysRegion> selectDistrictsByCityCode(@Param("cc") String cityCode);

    @Select("select * from sys_region where level = 4 order by code")
    List<SysRegion> selectStreets();

    @Select("select * from sys_region where level = 4 and pcode = #{dc} order by code")
    List<SysRegion> selectStreetsByDistrictCode(@Param("dc") String districtCode);

    @Select("select * from sys_region where level = 5 order by code")
    List<SysRegion> selectVillages();

    @Select("select * from sys_region where level = 5 and pcode = #{sc} order by code")
    List<SysRegion> selectVillagesByStreetCode(@Param("sc") String streetCode);

    @Select("select * from sys_region where code = #{code}")
    SysRegion selectByCode(@Param("code") String code);
}
