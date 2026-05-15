package org.xinhuamm.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * 应用数据源扩展配置。
 * 用于控制开发/测试环境中的 H2 降级能力。
 */
@Validated
@ConfigurationProperties(prefix = "app.datasource")
public class AppDatasourceProperties {

    /**
     * 是否允许 MySQL 不可用时降级到 H2。
     */
    private boolean fallbackEnabled;

    public boolean isFallbackEnabled() {
        return fallbackEnabled;
    }

    public void setFallbackEnabled(boolean fallbackEnabled) {
        this.fallbackEnabled = fallbackEnabled;
    }
}
