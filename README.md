# Spring Boot AI 脚手架模板

这是一个面向生产的 Spring Boot 标准化脚手架模板，集成了统一返回、全局异常、MyBatis、Redis 缓存抽象和日志追踪，支持数据库三环境配置与本地降级，适合团队与 AI 协作开发。

## 环境要求

- Java 21
- Gradle Wrapper（项目已内置）
- MySQL 8+（示例库）
- Redis 6+（可选，用于缓存示例）

## 快速启动

1. 准备数据库并执行初始化脚本：`src/main/resources/schema.sql`
2. 修改 `src/main/resources/application.yml` 与 `src/main/resources/application-*.yml` 中的数据库与 Redis 连接信息
3. 运行应用：`./gradlew bootRun`

默认 profile 为 `dev`。`dev/test` 环境中 MySQL 不可用时会降级到 H2，`prod` 环境禁止降级，数据库不可用时应直接启动失败。

## 关键能力

- 统一响应结构：`Result<T>`
- 全局异常处理：`GlobalExceptionHandler`
- MyBatis + XML 映射：`UserMapper.xml`
- Redis 缓存抽象：业务层通过 `CacheService` 调用缓存
- 数据库本地降级：`dev/test` 中 MySQL 不可用时自动降级为 H2
- Redis 自动开关：`app.redis.enabled` 控制启用，探测失败自动降级为空缓存
- 统一日志与 TraceId：`TraceIdFilter` + `logback-spring.xml`
- 统一线程池配置：`AsyncConfig`

## 示例接口

- 获取用户：`GET /api/v1/user/{id}`
- 新增用户：`POST /api/v1/user`

## 代码包目录

```
org.xinhuamm.demo
├── controller      # 控制层（对外接口）
├── service         # 业务逻辑层
├── service.impl
├── repository      # 数据访问层（MyBatis）
├── entity          # 数据库实体
├── dto             # 入参对象
├── vo              # 返回对象
├── config          # 配置类
├── common
│   ├── cache       # 缓存抽象
│   ├── response    # 统一返回结构
│   ├── exception   # 全局异常
│   └── util        # 工具类
└── DemoApplication
```

## 架构说明

详细代码架构图、运行流程图和部署拓扑图见 [docs/architecture.md](docs/architecture.md)。

模板定位决策见 [docs/decisions/0001-template-scope.md](docs/decisions/0001-template-scope.md)。

## 约定与规范

- Controller 仅负责参数接收与响应组装
- Service 负责业务逻辑
- 禁止直接返回实体类
- 缓存统一通过 `CacheService`
- 统一日志必须包含 `traceId`
- 并发任务必须使用统一线程池

## 说明

`ai-spec.yaml` 中包含 AI 协作规范与禁止路径约束，建议团队保持一致。

## AI 协作

将 `ai-spec.yaml` 内容配置到你的 AI 工具中：

- Cursor：复制到 `.cursorrules`
- Claude Project：粘贴到 Project Instructions
- GitHub Copilot：配置为 `.github/copilot-instructions.md`
