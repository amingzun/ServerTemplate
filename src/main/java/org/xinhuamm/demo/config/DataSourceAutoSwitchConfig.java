package org.xinhuamm.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * 数据源自动切换配置。
 * 自动探测 MySQL 是否可用，不可用时降级为 H2。
 */
@Slf4j
@Configuration
public class DataSourceAutoSwitchConfig {

    private final DataSourceProperties mysqlProperties;
    private final H2DataSourceProperties h2Properties;
    private final AppDatasourceProperties appDatasourceProperties;

    public DataSourceAutoSwitchConfig(DataSourceProperties mysqlProperties,
                                      H2DataSourceProperties h2Properties,
                                      AppDatasourceProperties appDatasourceProperties) {
        this.mysqlProperties = mysqlProperties;
        this.h2Properties = h2Properties;
        this.appDatasourceProperties = appDatasourceProperties;
    }

    /**
     * 构建主数据源，MySQL 优先，失败后仅在允许的环境降级到 H2。
     *
     * @return DataSource
     */
    @Bean
    @Primary
    public DataSource dataSource() {
        DataSource mysqlDataSource = mysqlProperties.initializeDataSourceBuilder().build();
        if (isAvailable(mysqlDataSource)) {
            log.info("检测到 MySQL 可用，使用 MySQL 数据源");
            return mysqlDataSource;
        }
        if (!appDatasourceProperties.isFallbackEnabled()) {
            throw new IllegalStateException("MySQL 不可用，且当前环境未开启 H2 降级");
        }

        DataSource h2DataSource = org.springframework.boot.jdbc.DataSourceBuilder.create()
                .url(h2Properties.getUrl())
                .driverClassName(h2Properties.getDriverClassName())
                .username(h2Properties.getUsername())
                .password(h2Properties.getPassword())
                .build();
        initH2Schema(h2DataSource);
        log.warn("MySQL 不可用，已自动切换到 H2 内存数据库");
        return h2DataSource;
    }

    private boolean isAvailable(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            return connection != null && connection.isValid(2);
        } catch (Exception ex) {
            log.warn("MySQL 探测失败，原因={}", ex.getMessage());
            return false;
        }
    }

    private void initH2Schema(DataSource dataSource) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("schema.sql"));
        populator.execute(dataSource);
    }
}
