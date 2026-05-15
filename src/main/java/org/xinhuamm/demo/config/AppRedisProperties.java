package org.xinhuamm.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * 应用 Redis 扩展配置。
 * 用于控制缓存能力是否启用。
 */
@Validated
@ConfigurationProperties(prefix = "app.redis")
public class AppRedisProperties {

    /**
     * 是否启用 Redis 缓存。
     */
    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
