package com.zqqiliyc.framework.web.constant;

import cn.hutool.core.lang.EnumItem;

/**
 * @author qili
 * @date 2025-09-29
 */
public interface DictItem<E extends Enum<E> & DictItem<E>> extends EnumItem<E> {

}
