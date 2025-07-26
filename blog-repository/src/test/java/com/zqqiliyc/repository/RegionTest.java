package com.zqqiliyc.repository;

import cn.hutool.core.date.StopWatch;
import com.zqqiliyc.domain.entity.SysRegion;
import com.zqqiliyc.repository.bean.*;
import com.zqqiliyc.repository.mapper.SysRegionMapper;
import org.apache.ibatis.datasource.DataSourceFactory;
import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.*;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author qili
 * @date 2025-07-25
 */
public class RegionTest {

    private static final StopWatch timer = new StopWatch("region build");

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        configuration.addMapper(SysRegionMapper.class);

        DataSourceFactory dataSourceFactory = new PooledDataSourceFactory();
        Properties properties = new Properties();
        properties.setProperty("driver", "com.mysql.cj.jdbc.Driver");
        properties.setProperty("url", "jdbc:mysql://localhost:3306/qiliblog?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true");
        properties.setProperty("username", "qiliblog");
        properties.setProperty("password", "qiliblog001");
        dataSourceFactory.setProperties(properties);

        Environment environment = new Environment("development", new JdbcTransactionFactory(), dataSourceFactory.getDataSource());

        configuration.setEnvironment(environment);

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

        resetTable(sqlSessionFactory);

        OriginalRegion originalRegion = new OriginalRegion();

        // 1. create province
        createProvince(originalRegion, sqlSessionFactory);

        // 2. create city
        createCity(originalRegion, sqlSessionFactory);

        // 3. create district
        createDistrict(originalRegion, sqlSessionFactory);

        // 4. create street
        createStreet(originalRegion, sqlSessionFactory);

        // 5. create village
        createVillage(originalRegion, sqlSessionFactory);

        finish();
    }

    private static void finish() {
        System.out.println(timer.prettyPrint(TimeUnit.MILLISECONDS));
    }

    private static void resetTable(SqlSessionFactory sqlSessionFactory) throws SQLException {
        try (SqlSession sqlSession = sqlSessionFactory.openSession();
             Connection connection = sqlSession.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("delete from sys_region where id > 0");
            statement.execute("alter table sys_region auto_increment = 1");
            statement.execute("analyze table sys_region");
        }
    }

    private static void createProvince(OriginalRegion originalRegion, SqlSessionFactory sqlSessionFactory) throws SQLException {
        timer.start("create province");
        List<Province> provinces = originalRegion.getProvinces();
        assertEquals(31, provinces.size());
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.REUSE, false)) {
            SysRegionMapper mapper = sqlSession.getMapper(SysRegionMapper.class);
            SysRegion sysRegion = new SysRegion();
            int count = 0;
            for (Province province : provinces) {
                sysRegion.setCode(province.getCode());
                sysRegion.setName(province.getName());
                sysRegion.setLevel(1);
                count += mapper.insert(sysRegion);
            }
            timer.stop();
            if (count == provinces.size()) {
                sqlSession.commit();
                System.out.println("insert province success, size " + provinces.size() + " inserted, cost " + timer.getLastTaskTimeMillis() + " ms");
            } else {
                sqlSession.rollback();
                System.out.println("insert province error, expected " + provinces.size() + ", but " + count + " inserted");
            }
        } catch (Exception e) {
            timer.stop();
            resetTable(sqlSessionFactory);
            throw new RuntimeException(e);
        }
    }

    private static void createCity(OriginalRegion originalRegion, SqlSessionFactory sqlSessionFactory) throws SQLException {
        timer.start("create city");
        List<City> cities = originalRegion.getCities();
        assertEquals(342, cities.size());
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.REUSE, false)) {
            SysRegionMapper mapper = sqlSession.getMapper(SysRegionMapper.class);
            SysRegion sysRegion = new SysRegion();
            int count = 0;
            for (City city : cities) {
                sysRegion.setCode(city.getCode());
                sysRegion.setName(city.getName());
                sysRegion.setLevel(2);
                sysRegion.setPcode(city.getProvinceCode());
                count += mapper.insert(sysRegion);
            }
            timer.stop();
            if (count == cities.size()) {
                sqlSession.commit();
                System.out.println("insert city success, size " + cities.size() + " inserted, cost " + timer.getLastTaskTimeMillis() + " ms");
            } else {
                sqlSession.rollback();
                System.out.println("insert city error, expected " + cities.size() + ", but " + count + " inserted");
            }
        } catch (Exception e) {
            timer.stop();
            resetTable(sqlSessionFactory);
            throw new RuntimeException(e);
        }
    }

    private static void createDistrict(OriginalRegion originalRegion, SqlSessionFactory sqlSessionFactory) throws SQLException {
        timer.start("create district");
        List<Area> districts = originalRegion.getAreas();
        assertEquals(2978, districts.size());
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.REUSE, false)) {
            SysRegionMapper mapper = sqlSession.getMapper(SysRegionMapper.class);
            int count = 0;
            // 分组批量插入
            for (int i = 0; i < districts.size(); i += 500) {
                List<Area> batch = districts.subList(i, Math.min(i + 500, districts.size()));
                Set<SysRegion> batchSysRegions = new HashSet<>();
                for (Area district : batch) {
                    SysRegion sysRegion = new SysRegion();
                    sysRegion.setCode(district.getCode());
                    sysRegion.setName(district.getName());
                    sysRegion.setLevel(3);
                    sysRegion.setPcode(district.getCityCode());
                    batchSysRegions.add(sysRegion);
                }
                count += mapper.batchInsert(batchSysRegions);
            }
            timer.stop();
            if (count == districts.size()) {
                sqlSession.commit();
                System.out.println("insert district success, size " + districts.size() + " inserted, cost " + timer.getLastTaskTimeMillis() + " ms");
            } else {
                sqlSession.rollback();
                System.out.println("insert district error, expected " + districts.size() + ", but " + count + " inserted");
            }
        } catch (Exception e) {
            timer.stop();
            resetTable(sqlSessionFactory);
            throw new RuntimeException(e);
        }
    }

    private static void createStreet(OriginalRegion originalRegion, SqlSessionFactory sqlSessionFactory) throws SQLException {
        timer.start("create street");
        List<Street> streets = originalRegion.getStreets();
        assertEquals(41352, streets.size());
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.REUSE, false)) {
            SysRegionMapper mapper = sqlSession.getMapper(SysRegionMapper.class);
            int count = 0;
            // 分组批量插入
            for (int i = 0; i < streets.size(); i += 500) {
                List<Street> batch = streets.subList(i, Math.min(i + 500, streets.size()));
                Set<SysRegion> batchSysRegions = new HashSet<>();
                for (Street street : batch) {
                    SysRegion sysRegion = new SysRegion();
                    sysRegion.setCode(street.getCode());
                    sysRegion.setName(street.getName());
                    sysRegion.setLevel(4);
                    sysRegion.setPcode(street.getAreaCode());
                    batchSysRegions.add(sysRegion);
                }
                count += mapper.batchInsert(batchSysRegions);
            }
            timer.stop();
            if (count == streets.size()) {
                sqlSession.commit();
                System.out.println("insert street success, size " + streets.size() + " inserted, cost " + timer.getLastTaskTimeMillis() + " ms");
            } else {
                sqlSession.rollback();
                System.out.println("insert street error, expected " + streets.size() + ", but " + count + " inserted");
            }
        } catch (Exception e) {
            timer.stop();
            resetTable(sqlSessionFactory);
            throw new RuntimeException(e);
        }
    }

    private static void createVillage(OriginalRegion originalRegion, SqlSessionFactory sqlSessionFactory) throws SQLException {
        timer.start("create village");
        List<Village> villages = originalRegion.getVillages();
        assertEquals(620573, villages.size());
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.REUSE, false)) {
            SysRegionMapper mapper = sqlSession.getMapper(SysRegionMapper.class);
            int count = 0;
            // 分组批量插入
            for (int i = 0; i < villages.size(); i += 500) {
                List<Village> batch = villages.subList(i, Math.min(i + 500, villages.size()));
                Set<SysRegion> batchSysRegions = new HashSet<>();
                for (Village village : batch) {
                    SysRegion sysRegion = new SysRegion();
                    sysRegion.setCode(village.getCode());
                    sysRegion.setName(village.getName());
                    sysRegion.setLevel(5);
                    sysRegion.setPcode(village.getStreetCode());
                    batchSysRegions.add(sysRegion);
                }
                count += mapper.batchInsert(batchSysRegions);
            }
            timer.stop();
            if (count == villages.size()) {
                sqlSession.commit();
                System.out.println("insert village success, size " + villages.size() + " inserted, cost " + timer.getLastTaskTimeMillis() + " ms");
            } else {
                sqlSession.rollback();
                System.out.println("insert village error, expected " + villages.size() + ", but " + count + " inserted");
            }
        } catch (Exception e) {
            timer.stop();
            resetTable(sqlSessionFactory);
            throw new RuntimeException(e);
        }
    }
}
