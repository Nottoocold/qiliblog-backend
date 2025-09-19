package com.zqqiliyc.framework.web.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * @author qili
 * @date 2025-09-16
 */
@Getter
@Setter
public class SortEntry implements Comparable<SortEntry> {
    /**
     * 排序字段
     */
    private String name;
    /**
     * 是否升序
     */
    private boolean asc;
    /**
     * 权重-值越小越靠前
     */
    private int weight;

    public SortEntry() {
    }

    public SortEntry(String name, boolean asc, int weight) {
        this.name = name;
        this.asc = asc;
        this.weight = weight;
    }

    @Override
    public int compareTo(SortEntry o) {
        return weight - o.weight;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SortEntry sortEntry)) return false;
        return Objects.equals(getName(), sortEntry.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }
}
