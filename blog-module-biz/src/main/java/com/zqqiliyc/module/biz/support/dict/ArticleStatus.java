package com.zqqiliyc.module.biz.support.dict;

import com.zqqiliyc.framework.web.constant.DictItem;

/**
 * @author qili
 * @date 2025-11-17
 */
public enum ArticleStatus implements DictItem<ArticleStatus> {
    DRAFT(0, "草稿"),
    PUBLISHED(1, "已发布"),
    PRIVATE(2, "私密");

    private final int code;

    private final String desc;

    ArticleStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public int intVal() {
        return code;
    }

    @Override
    public String text() {
        return desc;
    }

    @Override
    public ArticleStatus fromInt(Integer intVal) {
        for (ArticleStatus value : values()) {
            if (value.code == intVal) {
                return value;
            }
        }
        return null;
    }
}
