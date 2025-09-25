# Spring Cache 说明

启用Spring Cache的方式：

1. 引入spring-boot-starter-cache依赖
2. 在启动类或配置类中添加@EnableCaching

## 常用注解及其行为

- @Cacheable，标记在方法上。调用方法时，会先检查缓存。如果缓存中存在对应数据，则直接返回缓存数据，不再执行方法体；
  如果不存在，则执行方法，并将返回结果存入缓存。
  常用属性包括 value/cacheNames（指定缓存名称）、key（自定义缓存键，支持 SpEL 表达式）、condition（设置缓存条件）。
- @CachePut，标记在方法上。调用方法时，会先执行方法，然后将返回结果存入缓存。通常用于更新缓存。
- @CacheEvict，标记在方法上。用于从缓存中移除相应数据。allEntries属性可以设置为 true来移除指定缓存名称下的所有条目。
- @Caching，标记在方法上。组合多个缓存注解（如同时使用 @CacheEvict和 @CachePut）。

## 支持的缓存实现

Spring支持多种缓存提供者，只需引入对应的依赖并进行简单配置即可。

- Simple，默认的缓存实现，基于 ConcurrentHashMap，适用于单机、轻量级应用，不适合分布式环境。
- EhCache，一个纯 Java 的进程内缓存框架，功能丰富，在传统企业应用中非常流行。
    - 引入依赖：net.sf.ehcache:ehcache
- Caffeine，一个高性能的 Java 缓存库，通常被认为是 Guava Cache 的增强版，命中率和并发性能更优。
    - 引入依赖：com.github.ben-manes.caffeine:caffeine
- Redis，将数据存储于分布式缓存 Redis 中。适用于分布式、集群环境，共享缓存，可持久化。
- 等等
