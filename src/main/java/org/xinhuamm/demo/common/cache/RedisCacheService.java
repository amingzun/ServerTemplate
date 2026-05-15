package org.xinhuamm.demo.common.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.xinhuamm.demo.config.RedisAvailability;

import java.time.Duration;
import java.util.Optional;

/**
 * Redis 缓存服务实现。
 * Redis 不可用时自动降级为空缓存，不影响主业务链路。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisCacheService implements CacheService {

    private final ObjectProvider<RedisTemplate<String, Object>> redisTemplateProvider;
    private final RedisAvailability redisAvailability;

    @Override
    public <T> Optional<T> get(String key, Class<T> clazz) {
        if (!redisAvailability.isAvailable()) {
            return Optional.empty();
        }
        RedisTemplate<String, Object> redisTemplate = redisTemplateProvider.getIfAvailable();
        if (redisTemplate == null) {
            return Optional.empty();
        }
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (clazz.isInstance(value)) {
                return Optional.of(clazz.cast(value));
            }
            return Optional.empty();
        } catch (Exception ex) {
            log.warn("读取缓存失败，key={}，原因={}", key, ex.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void put(String key, Object value, Duration ttl) {
        if (!redisAvailability.isAvailable()) {
            return;
        }
        RedisTemplate<String, Object> redisTemplate = redisTemplateProvider.getIfAvailable();
        if (redisTemplate == null) {
            return;
        }
        try {
            redisTemplate.opsForValue().set(key, value, ttl);
        } catch (Exception ex) {
            log.warn("写入缓存失败，key={}，原因={}", key, ex.getMessage());
        }
    }
}

