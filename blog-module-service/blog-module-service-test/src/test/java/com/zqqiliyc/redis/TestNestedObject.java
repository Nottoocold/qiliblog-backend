package com.zqqiliyc.redis;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author qili
 * @date 2025-12-20
 */
@Getter
@Setter
class TestNestedObject {
    private Long id;
    private String name;
    private Integer age;
    private Date date;
    private LocalDateTime dateTime;
    private Map<String, Object> nested;

    public TestNestedObject() {
    }

    public TestNestedObject(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public void putNested(String key, Object value) {
        if (nested == null) {
            nested = new LinkedHashMap<>();
        }
        nested.put(key, value);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TestNestedObject that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName()) && Objects.equals(getAge(), that.getAge());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getAge());
    }
}
