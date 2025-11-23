package com.zqqiliyc.module.biz.config.mybatis;

import com.zqqiliyc.framework.web.json.JsonHelper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author qili
 * @date 2025-11-22
 */
@MappedTypes({Map.class})
@MappedJdbcTypes({JdbcType.OTHER})
public class PostgreSQLJsonTypeHandler extends BaseTypeHandler<Object> {
    private final Class<?> type;

    public PostgreSQLJsonTypeHandler(Class<?> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            ps.setNull(i, JdbcType.OTHER.TYPE_CODE);
        } else {
            PGobject jsonObject = new PGobject();
            jsonObject.setType("json");
            if (parameter instanceof String) {
                jsonObject.setValue(parameter.toString());
                ps.setObject(i, jsonObject);
                return;
            }
            jsonObject.setValue(JsonHelper.toJson(parameter));
            ps.setObject(i, jsonObject);
        }
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseJson(rs.getString(columnName));
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseJson(rs.getString(columnIndex));
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseJson(cs.getString(columnIndex));
    }

    private Object parseJson(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }

        if (type == String.class) {
            return json;
        }
        return JsonHelper.fromJson(json, type);
    }
}
