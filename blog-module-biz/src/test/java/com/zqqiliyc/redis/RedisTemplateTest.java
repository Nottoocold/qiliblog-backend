package com.zqqiliyc.redis;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.zqqiliyc.BaseTestApp;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author qili
 * @date 2025-12-07
 */
public class RedisTemplateTest extends BaseTestApp {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Test
    @Order(1)
    public void testSampleString() {
        redisTemplate.opsForValue().set("test", "test");
        Assertions.assertEquals("test", redisTemplate.opsForValue().get("test"));
        redisTemplate.delete("test");
        Assertions.assertNull(redisTemplate.opsForValue().get("test"));
    }

    @Test
    @Order(2)
    public void testNestedObjectString() {
        TestObject testObject = getTestObject();
        redisTemplate.opsForValue().set("test", testObject);
        Object storeValue = redisTemplate.opsForValue().get("test");
        Assertions.assertEquals(testObject, storeValue);
        redisTemplate.delete("test");
        Assertions.assertNull(redisTemplate.opsForValue().get("test"));

        TestNestedObject nestedObject = getTestNestedObject();
        redisTemplate.opsForValue().set("test", nestedObject);
        storeValue = redisTemplate.opsForValue().get("test");
        Assertions.assertEquals(nestedObject, storeValue);
        redisTemplate.delete("test");
        Assertions.assertNull(redisTemplate.opsForValue().get("test"));
    }

    @Test
    @Order(3)
    public void testHash() {
        redisTemplate.opsForHash().put("test", "key", "value");
        Assertions.assertEquals("value", redisTemplate.opsForHash().get("test", "key"));
        redisTemplate.delete("test");
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("key", "value");
        map.put("key1", 1);
        map.put("key2", getTestObject());
        map.put("key3", false);
        map.put("key4", LocalDateTimeUtil.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss"));
        map.put("key5", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        redisTemplate.opsForHash().putAll("test", map);
        Map<Object, Object> storeValue = redisTemplate.opsForHash().entries("test");
        Assertions.assertEquals(map, storeValue);
    }

    private TestObject getTestObject() {
        return new TestObject(Long.MAX_VALUE, "test", 18);
    }

    private TestNestedObject getTestNestedObject() {
        TestNestedObject nestedObject = new TestNestedObject(Long.MAX_VALUE / 4, "test", 18);
        nestedObject.setDate(new Date());
        nestedObject.setDateTime(LocalDateTime.now());
        nestedObject.putNested("nested", getTestObject());
        nestedObject.putNested("nested2", new TestObject(Long.MAX_VALUE / 2, "test", 18));
        nestedObject.putNested("nested3", "stringvalue");
        nestedObject.putNested("nested4", true);
        nestedObject.putNested("nested5", 18);
        return nestedObject;
     }
}

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