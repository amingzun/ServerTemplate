plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    java
}

group = "org.xinhuamm"
version = "1.0.0-SNAPSHOT"
description = "生产级 Spring Boot AI 脚手架模板"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.spring.web)
    implementation(libs.spring.validation)
    implementation(libs.spring.actuator)
    implementation(libs.spring.aop)
    implementation(libs.spring.data.redis)
    implementation(libs.spring.security)
    implementation(libs.mybatis.spring.boot.starter)
    implementation(libs.mysql.connector)
    implementation(libs.lombok)

    annotationProcessor(libs.lombok)

    runtimeOnly(libs.h2)

    testImplementation(libs.spring.boot.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
