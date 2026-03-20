package org.xinhuamm.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * H2 数据源配置。
 * 用于 MySQL 不可用时的自动降级。
 */
@ConfigurationProperties(prefix = "app.datasource.h2")
public class H2DataSourceProperties {

    /**
     * H2 JDBC URL。
     */
    private String url = "jdbc:h2:mem:demo_db;MODE=MySQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1";

    /**
     * H2 驱动类。
     */
    private String driverClassName = "org.h2.Driver";

    /**
     * 用户名。
     */
    private String username = "sa";

    /**
     * 密码。
     */
    private String password = "";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

