package org.xinhuamm.demo.common.cache;

import java.time.Duration;
import java.util.Optional;

/**
 * 缓存服务接口。
 * 业务层只依赖该接口，不感知 Redis 是否可用。
 */
public interface CacheService {

    /**
     * 根据 key 获取缓存。
     *
     * @param key   缓存键
     * @param clazz 目标类型
     * @param <T>   目标类型
     * @return 缓存值
     */
    <T> Optional<T> get(String key, Class<T> clazz);

    /**
     * 写入缓存。
     *
     * @param key 缓存键
     * @param value 缓存值
     * @param ttl 过期时间
     */
    void put(String key, Object value, Duration ttl);
}

