package org.xinhuamm.demo.common.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

/**
 * 空缓存实现。
 * 当 Redis 被关闭时使用，保证业务链路不依赖缓存。
 */
@Service
@ConditionalOnProperty(prefix = "app.redis", name = "enabled", havingValue = "false")
public class NoopCacheService implements CacheService {

    @Override
    public <T> Optional<T> get(String key, Class<T> clazz) {
        return Optional.empty();
    }

    @Override
    public void put(String key, Object value, Duration ttl) {
        // Redis 关闭时忽略缓存写入。
    }
}
