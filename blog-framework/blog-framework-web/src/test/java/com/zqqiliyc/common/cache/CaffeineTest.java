package com.zqqiliyc.common.cache;

import com.github.benmanes.caffeine.cache.*;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Caffeine缓存测试类
 *
 * @author qili
 * @date 2025-09-27
 */
public class CaffeineTest {

    /**
     * 基础缓存测试 - 手动加载
     */
    @Test
    public void testManualCache() {
        // 创建一个手动加载的缓存实例
        Cache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(100) // 最大缓存条目数
                .expireAfterWrite(1, TimeUnit.MINUTES) // 写入后1分钟过期
                .recordStats() // 开启统计功能
                .build();

        // 添加缓存项
        cache.put("key1", "value1");
        assertEquals("value1", cache.getIfPresent("key1"));

        // 获取不存在的key
        assertNull(cache.getIfPresent("key2"));

        // 使用get方法，如果key不存在则通过函数计算值
        String value = cache.get("key2", k -> "computed_" + k);
        assertEquals("computed_key2", value);

        // 移除缓存项
        cache.invalidate("key1");
        assertNull(cache.getIfPresent("key1"));
    }

    /**
     * 自动加载缓存测试
     */
    @Test
    public void testLoadingCache() {
        // 创建一个自动加载的缓存实例
        LoadingCache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .recordStats()
                .build(new CacheLoader<>() {
                    public String load(String key) {
                        System.out.println("Loading key: " + key);
                        return "loaded_" + key;
                    }

                    @Override
                    public Map<? extends String, ? extends String> loadAll(Set<? extends String> keys) throws Exception {
                        System.out.println("Loading keys: " + keys);
                        Map<String, String> result = new HashMap<>();
                        keys.forEach(key -> result.put(key, "loaded_" + key));
                        return result;
                    }
                });

        // 获取已存在的key
        assertEquals("loaded_test", cache.get("test"));

        // 批量获取
        Map<String, String> map = cache.getAll(java.util.Arrays.asList("key1", "key2"));
        assertEquals("loaded_key1", map.get("key1"));
        assertEquals("loaded_key2", map.get("key2"));
    }

    /**
     * 异步缓存测试
     */
    @Test
    public void testAsyncCache() throws ExecutionException, InterruptedException {
        // 创建异步缓存实例
        AsyncCache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .buildAsync();

        // 直接放入CompletableFuture
        cache.put("key1", CompletableFuture.completedFuture("value1"));
        assertEquals("value1", cache.getIfPresent("key1").get());

        // 使用异步加载方式
        AsyncLoadingCache<String, String> asyncLoadingCache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .buildAsync(key -> "async_loaded_" + key);

        CompletableFuture<String> future = asyncLoadingCache.get("key2");
        assertEquals("async_loaded_key2", future.get());
    }

    /**
     * 缓存回收策略测试
     */
    @Test
    public void testEvictionPolicy() throws InterruptedException {
        // 基于大小的回收策略
        Cache<String, String> sizeBasedCache = Caffeine.newBuilder()
                .maximumSize(2) // 最多保留2个元素
                .build();

        sizeBasedCache.put("key1", "value1");
        sizeBasedCache.put("key2", "value2");
        sizeBasedCache.put("key3", "value3");

        // 触发清理
        sizeBasedCache.cleanUp();

        // 由于容量限制，可能会移除一些条目，但具体哪一个被移除取决于实现细节

        // 基于时间的回收策略
        Cache<String, String> timeBasedCache = Caffeine.newBuilder()
                .expireAfterWrite(100, TimeUnit.MILLISECONDS) // 写入后100毫秒过期
                .build();

        timeBasedCache.put("key1", "value1");
        assertEquals("value1", timeBasedCache.getIfPresent("key1"));

        Thread.sleep(150); // 等待超过过期时间
        assertNull(timeBasedCache.getIfPresent("key1")); // 应该已经过期

        // 基于访问时间的过期策略
        Cache<String, String> accessTimeCache = Caffeine.newBuilder()
                .expireAfterAccess(100, TimeUnit.MILLISECONDS) // 访问后100毫秒过期
                .build();

        accessTimeCache.put("key1", "value1");
        assertEquals("value1", accessTimeCache.getIfPresent("key1"));

        Thread.sleep(50);
        // 在100毫秒内访问，延长过期时间
        assertEquals("value1", accessTimeCache.getIfPresent("key1"));

        Thread.sleep(150); // 超过100毫秒未访问
        assertNull(accessTimeCache.getIfPresent("key1")); // 应该已经过期
    }

    /**
     * 测试写入后过期时间是否会重置
     */
    @Test
    public void testExpireAfterWriteReset() throws InterruptedException {
        Cache<String, String> cache = Caffeine.newBuilder()
                .expireAfterWrite(100, TimeUnit.MILLISECONDS)
                .build();

        // 第一次放入key1
        cache.put("key1", "value1");
        assertEquals("value1", cache.getIfPresent("key1"));

        // 等待50毫秒（未过期）
        Thread.sleep(50);
        assertEquals("value1", cache.getIfPresent("key1"), "在50毫秒后key1应该仍然存在");

        // 在过期前再次放入相同的key（这会重置过期时间）
        cache.put("key1", "value1_updated");
        assertEquals("value1_updated", cache.getIfPresent("key1"), "更新后的值应该能获取到");

        // 再等待50毫秒（总共100毫秒，但由于重置了时间，应该还没过期）
        Thread.sleep(50);
        assertEquals("value1_updated", cache.getIfPresent("key1"), "重置后100毫秒，key1应该仍然存在");

        // 再等待100毫秒（确保过期）
        Thread.sleep(100);
        assertNull(cache.getIfPresent("key1"), "再过100毫秒后key1应该已经过期");
    }

    /**
     * 测试固定过期时间（不会因为读写而重置）
     */
    @Test
    public void testFixedExpirationTime() throws InterruptedException {
        // 使用自定义Expiry策略实现固定过期时间
        Cache<String, String> cache = Caffeine.newBuilder()
                .expireAfter(new Expiry<String, String>() {
                    @Override
                    public long expireAfterCreate(String key, String value, long currentTime) {
                        // 设置固定过期时间为创建后200毫秒
                        return TimeUnit.MILLISECONDS.toNanos(200);
                    }

                    @Override
                    public long expireAfterUpdate(String key, String value, long currentTime, long currentDuration) {
                        // 更新时不改变过期时间，返回当前持续时间
                        return currentDuration;
                    }

                    @Override
                    public long expireAfterRead(String key, String value, long currentTime, long currentDuration) {
                        // 读取时不改变过期时间，返回当前持续时间
                        return currentDuration;
                    }
                })
                .build();

        // 放入key1
        long startTime = System.currentTimeMillis();
        cache.put("key1", "value1");
        assertEquals("value1", cache.getIfPresent("key1"));

        // 等待100毫秒后更新key1
        Thread.sleep(100);
        cache.put("key1", "value1_updated");
        assertEquals("value1_updated", cache.getIfPresent("key1"), "更新后的值应该能获取到");

        // 再等待150毫秒（总共250毫秒，超过了固定过期时间200毫秒）
        Thread.sleep(150);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // 验证key1已经过期（因为超过了固定的200毫秒过期时间）
        assertNull(cache.getIfPresent("key1"),
                "即使在过期前更新了值，key1也应该在固定的200毫秒后过期，耗时: " + duration + "毫秒");
    }

    /**
     * 弱引用键和软引用值测试
     */
    @Test
    public void testReferenceTypes() {
        // 弱引用键 - 当键没有其他强引用时可以被垃圾回收
        Cache<String, String> weakKeysCache = Caffeine.newBuilder()
                .weakKeys()
                .build();

        String key = new String("key1"); // 显式创建新对象避免字符串常量池
        weakKeysCache.put(key, "value1");
        assertEquals("value1", weakKeysCache.getIfPresent(key));

        // 清除强引用
        key = null;
        // 注意：实际的垃圾回收行为可能不会立即发生，这里只是测试
        System.gc();
        // 验证cache的条目数
        weakKeysCache.cleanUp();
        assertEquals(0, weakKeysCache.estimatedSize());

        // 软引用值 - 当内存不足时可以被垃圾回收
        Cache<String, String> softValuesCache = Caffeine.newBuilder()
                .softValues()
                .build();

        softValuesCache.put("key1", "value1");
        assertEquals("value1", softValuesCache.getIfPresent("key1"));
    }

    /**
     * 移除监听器测试
     */
    @Test
    public void testRemovalListener() throws InterruptedException {
        StringBuilder removedKey = new StringBuilder();
        StringBuilder removedValue = new StringBuilder();
        StringBuilder removalCause = new StringBuilder();

        Cache<String, String> cache = Caffeine.newBuilder()
                .removalListener((String key, String value, RemovalCause cause) -> {
                    System.out.println("Removal listener triggered, thread=" + Thread.currentThread().getName());
                    removedKey.append(key);
                    removedValue.append(value);
                    removalCause.append(cause);
                    System.out.println("Removed: " + key + ", " + value + ", " + cause);
                })
                .build();

        cache.put("key1", "value1");
        assertEquals("value1", cache.getIfPresent("key1"));

        cache.invalidate("key1"); // 主动移除

        // 等待移除监听器执行完成
        Thread.sleep(100);

        assertEquals("key1", removedKey.toString());
        assertEquals("value1", removedValue.toString());
        assertEquals(RemovalCause.EXPLICIT.toString(), removalCause.toString());
    }

    /**
     * 统计信息测试
     */
    @Test
    public void testStatistics() {
        Cache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(100)
                .recordStats()
                .build();

        cache.put("key1", "value1");
        assertEquals("value1", cache.getIfPresent("key1"));
        cache.getIfPresent("key2");

        // 获取统计信息
        CacheStats stats = cache.stats();
        assertTrue(stats.hitCount() >= 1);
        assertTrue(stats.missCount() >= 1);
        assertTrue(stats.requestCount() >= 2);
    }

    /**
     * 刷新机制测试
     * refreshAfterWrite机制说明：
     * 1. 当缓存项被访问且距离上次写入时间超过指定时间时，触发刷新
     * 2. 刷新是异步进行的，不会阻塞当前线程
     * 3. 在刷新过程中，旧值仍然可用
     * 4. 刷新成功后，新值会替换旧值
     * 5. 如果刷新失败，旧值仍然保留
     */
    @Test
    public void testRefresh() throws Exception {
        AtomicInteger loadCount = new AtomicInteger(0);

        LoadingCache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(100)
                .refreshAfterWrite(100, TimeUnit.MILLISECONDS) // 100毫秒后刷新
                .build(key -> {
                    int count = loadCount.incrementAndGet();
                    return "refreshed_" + key + "_v" + count;
                });

        // 第一次获取，触发加载
        String value1 = cache.get("key1");
        assertEquals("refreshed_key1_v1", value1);
        assertEquals(1, loadCount.get());

        // 等待超过刷新时间
        Thread.sleep(150);

        // 再次获取，应该触发刷新（异步）
        String value2 = cache.get("key1");
        assertEquals("refreshed_key1_v2", value2);
        // loadCount可能已经增加到2（刷新操作）
        assertTrue(loadCount.get() >= 2);

        // 等待50毫秒
        Thread.sleep(50);

        // 获取，仍然是旧值
        String value3 = cache.get("key1");
        assertEquals("refreshed_key1_v2", value3);
        assertTrue(loadCount.get() >= 2);

        // 手动刷新
        cache.refresh("key1");
        Thread.sleep(50); // 等待刷新完成
        String value4 = cache.get("key1");
        assertEquals("refreshed_key1_v3", value4); // 又是新值

        // 等待200毫秒
        Thread.sleep(200);

        String value5 = cache.get("key1");
        assertEquals("refreshed_key1_v4", value5);
        assertTrue(loadCount.get() >= 4);

        // 最终loadCount应该为4
        assertEquals(4, loadCount.get());
    }
}