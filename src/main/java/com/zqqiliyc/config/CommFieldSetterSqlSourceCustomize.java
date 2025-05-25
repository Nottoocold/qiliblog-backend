package com.zqqiliyc.config;

import com.zqqiliyc.domain.entity.BaseEntity;
import io.mybatis.provider.EntityTable;
import io.mybatis.provider.SqlSourceCustomize;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * @author qili
 * @date 2025-05-25
 */
public class CommFieldSetterSqlSourceCustomize implements SqlSourceCustomize {
    private static final String createdTime = "createTime";
    private static final String updatedTime = "updateTime";

    @Override
    public SqlSource customize(SqlSource sqlSource, EntityTable entity, MappedStatement ms, ProviderContext context) {
        return parameterObject -> {
            SqlCommandType sqlCommandType = ms.getSqlCommandType();
            if (sqlCommandType == SqlCommandType.INSERT || sqlCommandType == SqlCommandType.UPDATE) {
                MetaObject metaObject = ms.getConfiguration().newMetaObject(parameterObject);
                LocalDateTime now = LocalDateTime.now();
                if (parameterObject instanceof BaseEntity && sqlCommandType == SqlCommandType.INSERT) {
                    setCommonField(metaObject, createdTime, now);
                    setCommonField(metaObject, updatedTime, now);
                }
                if (sqlCommandType == SqlCommandType.UPDATE) {
                    if (parameterObject instanceof BaseEntity) {
                        setCommonField(metaObject, updatedTime, now);
                    } else {
                        // 处理非BaseEntity子类也需要自动填充的字段
                        if (metaObject.hasSetter(updatedTime)) {
                            setCommonField(metaObject, updatedTime, now);
                        }
                    }
                }
            }
            return sqlSource.getBoundSql(parameterObject);
        };
    }

    private void setCommonField(MetaObject metaObject, String field, Object value) {
        Object val = metaObject.getValue(field);
        if (val == null) {
            metaObject.setValue(field, value);
        }
    }
}
