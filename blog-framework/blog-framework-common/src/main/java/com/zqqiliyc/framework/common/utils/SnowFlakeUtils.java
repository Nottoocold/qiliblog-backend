package com.zqqiliyc.framework.common.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * @author qili
 * @date 2025-04-05
 */
public class SnowFlakeUtils {
    private static Snowflake snowflake = init();

    private static synchronized Snowflake init() {
        return snowflake = IdUtil.getSnowflake(1, 1);
    }

    public static long genId() {
        return snowflake.nextId();
    }

    public static String genIdStr() {
        return snowflake.nextIdStr();
    }
}
