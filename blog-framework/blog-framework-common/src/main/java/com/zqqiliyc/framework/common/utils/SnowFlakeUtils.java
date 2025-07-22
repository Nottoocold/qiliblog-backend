package com.zqqiliyc.framework.common.utils;

import com.github.yitter.contract.IIdGenerator;
import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.DefaultIdGenerator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qili
 * @date 2025-04-05
 */
@Slf4j
public class SnowFlakeUtils {
    private static final IIdGenerator idGenInstance = init();

    private static synchronized IIdGenerator init() {
        return new DefaultIdGenerator(new IdGeneratorOptions((short) 1));
    }

    public static long genId() {
        return idGenInstance.newLong();
    }

    public static String genIdStr() {
        return Long.toString(genId());
    }
}
