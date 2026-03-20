package org.xinhuamm.demo.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * Redis 可用性检测。
 * 自动判断 Redis 是否可用，作为缓存开关的运行时依据。
 */
@Slf4j
@Configuration
public class RedisAvailability {

    private final ObjectProvider<RedisConnectionFactory> connectionFactoryProvider;

    @Value("${app.redis.enabled:true}")
    private boolean redisEnabled;

    private volatile boolean available;

    public RedisAvailability(ObjectProvider<RedisConnectionFactory> connectionFactoryProvider) {
        this.connectionFactoryProvider = connectionFactoryProvider;
    }

    @PostConstruct
    public void detect() {
        if (!redisEnabled) {
            available = false;
            log.warn("Redis 已在配置中关闭");
            return;
        }
        RedisConnectionFactory factory = connectionFactoryProvider.getIfAvailable();
        if (factory == null) {
            available = false;
            log.warn("未检测到 Redis 连接工厂，缓存自动关闭");
            return;
        }
        try (RedisConnection connection = factory.getConnection()) {
            String pong = connection.ping();
            available = "PONG".equalsIgnoreCase(pong);
            if (available) {
                log.info("Redis 可用，缓存已启用");
            } else {
                log.warn("Redis ping 失败，缓存自动关闭");
            }
        } catch (Exception ex) {
            available = false;
            log.warn("Redis 探测失败，缓存自动关闭，原因={}", ex.getMessage());
        }
    }

    public boolean isAvailable() {
        return available;
    }
}

