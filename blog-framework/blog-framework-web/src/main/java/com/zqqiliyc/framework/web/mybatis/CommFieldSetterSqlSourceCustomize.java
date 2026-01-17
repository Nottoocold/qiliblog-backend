package com.zqqiliyc.framework.web.mybatis;

import cn.hutool.core.util.StrUtil;
import com.zqqiliyc.framework.common.utils.SnowFlakeUtils;
import com.zqqiliyc.framework.web.domain.entity.BaseEntity;
import io.mybatis.provider.EntityTable;
import io.mybatis.provider.SqlSourceCustomize;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * 实体类通用字段自动填充
 *
 * @author qili
 * @date 2025-05-25
 */
@Slf4j
public class CommFieldSetterSqlSourceCustomize implements SqlSourceCustomize {
    private static final String createdTime = "createTime";
    private static final String updatedTime = "updateTime";
    private static final String id = "id";

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
                    setCommonField(metaObject, id, SnowFlakeUtils.genId());
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
        try {
            Object val = metaObject.getValue(field);
            if (val == null) {
                metaObject.setValue(field, value);
            }
        } catch (BindingException ignored) {
            if (log.isDebugEnabled()) {
                String msg = String.format("when call %s.set%s, maybe %s is not exist",
                        metaObject.getOriginalObject().getClass().getName(), StrUtil.upperFirst(field), field);
                log.warn(msg);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
