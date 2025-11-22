package com.zqqiliyc.biz.core.config.mybatis;

import io.mybatis.common.util.Assert;
import io.mybatis.mapper.logical.LogicalColumn;
import io.mybatis.mapper.logical.LogicalMapper;
import io.mybatis.provider.EntityColumn;
import io.mybatis.provider.EntityTable;
import io.mybatis.provider.SqlScript;
import io.mybatis.provider.defaults.AnnotationSqlWrapper;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.util.List;

/**
 * @author qili
 * @date 2025-06-01
 */
public class LogicalOptimizeSqlWrapper extends AnnotationSqlWrapper {

    public LogicalOptimizeSqlWrapper(Object target, ElementType type, Annotation[] annotations) {
        super(target, type, annotations);
    }

    @Override
    public SqlScript wrap(ProviderContext context, EntityTable entity, SqlScript sqlScript) {
        if (LogicalMapper.class.isAssignableFrom(context.getMapperType())) {
            return new MyLogicalSqlScript(sqlScript);
        }
        return sqlScript;
    }

    static class MyLogicalSqlScript implements SqlScript {
        private final SqlScript originSqlScript;

        public MyLogicalSqlScript(SqlScript originSqlScript) {
            this.originSqlScript = originSqlScript;
        }

        @Override
        public String getSql(EntityTable entity) {
            String sql = originSqlScript.getSql(entity);
            EntityColumn logicalColumn = getLogicalColumn(entity);
            String sqlPart = columnNotEqualsValueCondition(logicalColumn, deleteValue(logicalColumn));
            String replaced = columnEqualsValueCondition(logicalColumn, "0");
            return sql.replace(sqlPart, replaced);
        }

        private static String deleteValue(EntityColumn logicColumn) {
            return logicColumn.field().getAnnotation(LogicalColumn.class).delete();
        }

        private static EntityColumn getLogicalColumn(EntityTable entity) {
            List<EntityColumn> logicColumns = entity.columns().stream().filter(c -> c.field().isAnnotationPresent(LogicalColumn.class)).toList();
            Assert.isTrue(logicColumns.size() == 1, "There are no or multiple fields marked with @LogicalColumn");
            return logicColumns.get(0);
        }

        private static String columnNotEqualsValueCondition(EntityColumn c, String value) {
            return " " + c.column() + choiceNotEqualsOperator(value) + value;
        }

        private static String choiceNotEqualsOperator(String value) {
            if ("null".compareToIgnoreCase(value) == 0) {
                return " IS NOT ";
            }
            return " != ";
        }

        private static String columnEqualsValueCondition(EntityColumn c, String value) {
            return " " + c.column() + choiceEqualsOperator(value) + value + " ";
        }

        private static String choiceEqualsOperator(String value) {
            if ("null".compareToIgnoreCase(value) == 0) {
                return " IS ";
            }
            return " = ";
        }
    }
}
