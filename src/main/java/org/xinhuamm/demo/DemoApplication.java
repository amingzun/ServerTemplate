package org.xinhuamm.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.xinhuamm.demo.config.AppDatasourceProperties;
import org.xinhuamm.demo.config.AppRedisProperties;
import org.xinhuamm.demo.config.H2DataSourceProperties;

/**
 * 应用启动类。
 * 负责启动 Spring Boot 应用并扫描 MyBatis Mapper。
 */
@SpringBootApplication
@MapperScan("org.xinhuamm.demo.repository")
@EnableAsync
@EnableConfigurationProperties({
        AppDatasourceProperties.class,
        AppRedisProperties.class,
        H2DataSourceProperties.class
})
public class DemoApplication {

    /**
     * 应用主入口。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
