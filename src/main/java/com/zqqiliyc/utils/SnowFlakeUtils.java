package com.zqqiliyc.utils;

import com.github.yitter.contract.IIdGenerator;
import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.DefaultIdGenerator;
import io.mybatis.provider.EntityColumn;
import io.mybatis.provider.EntityTable;
import io.mybatis.provider.keysql.GenId;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qili
 * @date 2025-04-05
 */
@Slf4j
public class SnowFlakeUtils implements GenId<Long> {
    private static IIdGenerator idGenInstance = null;

    public static synchronized void init() {
        if (idGenInstance == null) {
            try {
                idGenInstance = new DefaultIdGenerator(new IdGeneratorOptions((short) 1));
            } catch (Exception e) {
                log.error("初始化雪花算法失败", e);
            }
        }
    }

    public SnowFlakeUtils() {
        init();
    }

    @Override
    public Long genId(EntityTable table, EntityColumn column) {
        return idGenInstance.newLong();
    }
}
