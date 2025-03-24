package com.zqqiliyc.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.zqqiliyc.domain.entity.Entity;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * 通用字段填充处理器
 *
 * @author qili
 * @date 2025-03-25
 */
public class MetaFieldHandler implements MetaObjectHandler {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MetaFieldHandler.class);

    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.getOriginalObject() instanceof Entity entity) {
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(entity.getCreateTime());
            entity.setDelFlag(Entity.DEL_FLAG_NORMAL);
            logger.debug("insertFill {}'s common fields.", entity.getClass().getSimpleName());
        }
    }

    /**
     * 更新元对象字段填充（用于更新时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.getOriginalObject() instanceof Entity entity) {
            if (!entity.canUpdate()) {
                String msg = "because of " + entity.getClass().getName() + "'s delFlag = " + entity.getDelFlag() + ", so can't update";
                throw new RuntimeException(msg);
            }
            entity.setUpdateTime(LocalDateTime.now());
            logger.debug("updateFill {}'s common fields.", entity.getClass().getSimpleName());
        }
    }
}
