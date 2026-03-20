package org.xinhuamm.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 应用启动类。
 * 负责启动 Spring Boot 应用并扫描 MyBatis Mapper。
 */
@SpringBootApplication
@MapperScan("org.xinhuamm.demo.repository")
@EnableAsync
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
