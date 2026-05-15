package org.xinhuamm.demo.common.cache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;

/**
 * NoopCacheService 单元测试。
 * 验证 Redis 关闭时缓存层不会影响主链路。
 */
class NoopCacheServiceTest {

    @Test
    void shouldAlwaysReturnEmptyWhenCacheDisabled() {
        CacheService cacheService = new NoopCacheService();

        cacheService.put("user:1", "value", Duration.ofMinutes(1));

        Assertions.assertTrue(cacheService.get("user:1", String.class).isEmpty());
    }
}
