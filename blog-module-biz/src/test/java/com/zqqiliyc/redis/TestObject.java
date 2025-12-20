package com.zqqiliyc.redis;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * @author qili
 * @date 2025-12-20
 */
@Getter
@Setter
class TestObject {
    private Long id;
    private String name;
    private Integer age;

    public TestObject() {
    }

    public TestObject(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TestObject that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName()) && Objects.equals(getAge(), that.getAge());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getAge());
    }
}
