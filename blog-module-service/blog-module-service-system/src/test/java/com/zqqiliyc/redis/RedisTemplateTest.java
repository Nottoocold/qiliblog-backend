package com.zqqiliyc.redis;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.zqqiliyc.BaseTestApp;
import com.zqqiliyc.framework.web.bean.AuthUserInfoBean;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qili
 * @date 2025-12-07
 */
public class RedisTemplateTest extends BaseTestApp {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    void setup() {
        redisTemplate.delete("test");
    }

    @AfterEach
    void tearDown() {
        redisTemplate.delete("test");
        Assertions.assertNull(redisTemplate.opsForValue().get("test"));
    }

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
        map.put("key5", new Date());
        redisTemplate.opsForHash().putAll("test", map);
        Map<Object, Object> storeValue = redisTemplate.opsForHash().entries("test");
        Assertions.assertEquals(map.size(), storeValue.size());
        map.forEach((k, v) -> {
            if (v instanceof Date) {
                // For Date objects, compare the time values instead of the objects themselves
                Assertions.assertEquals(((Date) v).getTime(), ((Date) storeValue.get(k)).getTime());
            } else {
                Assertions.assertEquals(v, storeValue.get(k));
            }
        });
    }

    @Test
    @Order(4)
    public void testList() {
        for (int i = 0; i < 10; i++) {
            redisTemplate.opsForList().leftPush("test", "element" + i);
        }
        List<Object> storeValue = redisTemplate.opsForList().range("test", 0, -1);
        Assertions.assertNotNull(storeValue);
        Assertions.assertEquals(10, storeValue.size());
    }

    @Test
    @Order(5)
    public void testSessionUser() {
        AuthUserInfoBean authUserInfoBean = new AuthUserInfoBean();
        authUserInfoBean.setId(Long.MAX_VALUE);
        authUserInfoBean.setUsername("jack chen");
        authUserInfoBean.setNickname("jack chen");
        authUserInfoBean.setAvatar("https://avatars.githubusercontent.com/u/10656201?v=4");
        authUserInfoBean.setRoles(CollUtil.newHashSet("ROLE_USER"));
        authUserInfoBean.setPermissions(CollUtil.newHashSet("WRITE", "READ", "DELETE"));
        redisTemplate.opsForValue().set("authUser:" + authUserInfoBean.getId(), authUserInfoBean);
        AuthUserInfoBean storeValue = (AuthUserInfoBean) redisTemplate.opsForValue().get("authUser:" + authUserInfoBean.getId());
        Assertions.assertNotNull(storeValue);
        Assertions.assertEquals(authUserInfoBean, storeValue);

        redisTemplate.opsForHash().putAll("sessionUser:" + authUserInfoBean.getId(), authUserInfoBean.toMap());
        Map<Object, Object> storeValueMap = redisTemplate.opsForHash().entries("sessionUser:" + authUserInfoBean.getId());
        Assertions.assertNotNull(storeValueMap);
        Assertions.assertEquals(authUserInfoBean, AuthUserInfoBean.fromMap(storeValueMap));
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