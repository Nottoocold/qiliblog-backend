package com.zqqiliyc.repository;

import com.zqqiliyc.repository.bean.*;
import org.apache.ibatis.datasource.DataSourceFactory;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * <a href="https://github.com/modood/Administrative-divisions-of-China">data.sqlite的获取地址</a>
 *
 * @author qili
 * @date 2025-07-26
 */
public class OriginalRegion {
    private final SqlSessionFactory sqlSessionFactory;

    public OriginalRegion() {
        DataSourceFactory dataSourceFactory = new UnpooledDataSourceFactory();
        Properties properties = new Properties();
        properties.setProperty("driver", "org.sqlite.JDBC");
        properties.setProperty("url", "jdbc:sqlite:log/data.sqlite");
        dataSourceFactory.setProperties(properties);
        Environment environment = new Environment("get", new JdbcTransactionFactory(), dataSourceFactory.getDataSource());
        Configuration configuration = new Configuration(environment);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }

    public List<Province> getProvinces() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession();
             Statement statement = sqlSession.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery("select code, name from province");
            List<Province> provinces = new ArrayList<>();
            while (resultSet.next()) {
                Province province = new Province();
                province.setCode(resultSet.getString("code"));
                province.setName(resultSet.getString("name"));
                provinces.add(province);
            }
            return provinces;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<City> getCities() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession();
             Statement statement = sqlSession.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery("select code, name, provinceCode from city");
            List<City> cities = new ArrayList<>();
            while (resultSet.next()) {
                City city = new City();
                city.setCode(resultSet.getString("code"));
                city.setName(resultSet.getString("name"));
                city.setProvinceCode(resultSet.getString("provinceCode"));
                cities.add(city);
            }
            return cities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Area> getAreas() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession();
             Statement statement = sqlSession.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery("select code, name, cityCode, provinceCode from area");
            List<Area> areas = new ArrayList<>();
            while (resultSet.next()) {
                Area area = new Area();
                area.setCode(resultSet.getString("code"));
                area.setName(resultSet.getString("name"));
                area.setCityCode(resultSet.getString("cityCode"));
                area.setProvinceCode(resultSet.getString("provinceCode"));
                areas.add(area);
            }
            return areas;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Street> getStreets() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession();
             Statement statement = sqlSession.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery("select code, name, areaCode, cityCode, provinceCode from street");
            List<Street> streets = new ArrayList<>();
            while (resultSet.next()) {
                Street street = new Street();
                street.setCode(resultSet.getString("code"));
                street.setName(resultSet.getString("name"));
                street.setAreaCode(resultSet.getString("areaCode"));
                street.setCityCode(resultSet.getString("cityCode"));
                street.setProvinceCode(resultSet.getString("provinceCode"));
                streets.add(street);
            }
            return streets;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Village> getVillages() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession();
             Statement statement = sqlSession.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery("select code, name, streetCode, areaCode, cityCode, provinceCode from village");
            List<Village> villages = new ArrayList<>();
            while (resultSet.next()) {
                Village village = new Village();
                village.setCode(resultSet.getString("code"));
                village.setName(resultSet.getString("name"));
                village.setStreetCode(resultSet.getString("streetCode"));
                village.setAreaCode(resultSet.getString("areaCode"));
                village.setCityCode(resultSet.getString("cityCode"));
                village.setProvinceCode(resultSet.getString("provinceCode"));
                villages.add(village);
            }
            return villages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
