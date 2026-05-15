# 服务端脚手架优化设计

## 背景

当前项目是一个 Spring Boot AI 协作脚手架，已经具备统一返回、全局异常、MyBatis XML、Redis 缓存抽象、TraceId 日志、三环境配置，以及 dev/test 数据库降级能力。

本次设计目标不是继续堆叠企业平台能力，而是把脚手架稳定在“中等生产模板”的定位：能直接启动业务开发，保留常用基础设施，减少演示代码和过度抽象，让人类开发者与 AI 都能快速理解边界。

## 定位

脚手架定位为：中等生产级 Spring Boot AI 协作基线。

它不是企业平台，不默认加入限流、审计、权限、消息队列、分布式事务、OpenAPI、Docker 或 CI。它也不是教学 demo，而是面向真实业务项目的后端起点。

核心原则：

- 模板只提供业务开发高频需要的基础设施。
- 可选能力保留扩展入口，不默认占用业务接口。
- dev/test 可以提供便利降级，prod 必须诚实失败。
- AI 规范只约束已有能力，不描述尚未接入的技术。

## 架构边界

保留以下包结构：

```text
org.xinhuamm.demo
├── controller      # 控制层，仅处理入参、校验、响应
├── service         # 业务接口
├── service.impl    # 业务实现
├── repository      # MyBatis 数据访问
├── entity          # 数据库实体
├── dto             # 入参对象
├── vo              # 返回对象
├── config          # 框架配置
├── common
│   ├── cache       # 缓存抽象
│   ├── response    # 统一返回
│   ├── exception   # 全局异常
│   └── util        # 横切工具
└── DemoApplication
```

分层规则：

- Controller 只负责 HTTP 入参、校验和 `Result<T>` 响应组装。
- Service 承载业务逻辑，只返回 VO，不直接暴露 Entity。
- Repository 只做 MyBatis 数据访问，SQL 统一放 XML。
- 缓存统一通过 `CacheService`，业务层不直接依赖 `RedisTemplate`。
- 线程池配置保留为基础设施，不默认提供无业务意义的演示接口。

## 配置与环境策略

配置采用“主配置放通用默认值，profile 放环境差异”的规则。

`application.yml`：

- 应用名、端口、日志级别。
- 通用 HikariCP 参数。
- Redis 默认连接占位符。
- `spring.profiles.default: dev`。
- 默认关闭数据库降级：`app.datasource.fallback-enabled: false`。

`application-dev.yml`：

- 允许 `app.datasource.fallback-enabled: true`。
- MySQL 不可用时自动切换到 H2。
- Redis 默认开启，但不可用时通过缓存抽象降级为空缓存。

`application-test.yml`：

- 允许 H2 fallback。
- Redis 默认关闭或降级为空缓存。
- 后续可补测试专用数据初始化。

`application-prod.yml`：

- 禁止 H2 fallback。
- 敏感配置全部来自环境变量。
- 数据库不可用时启动失败。

建议后续引入类型化配置：

- `AppDatasourceProperties` 管理 `app.datasource`。
- `AppRedisProperties` 管理 `app.redis`。
- 使用 `@ConfigurationProperties` 与 `@Validated` 做启动期校验。

## 数据库设计

保留 `DataSourceAutoSwitchConfig`，但降级边界必须明确：

- MySQL 优先。
- 仅 dev/test 允许 fallback 到 H2。
- prod 禁止 fallback。
- H2 只作为开发和测试便利，不作为生产兜底能力。
- `schema.sql` 保持 MySQL/H2 兼容语法。

建议后续把 `@Value("${app.datasource.fallback-enabled:false}")` 替换为类型化配置，减少散落字符串配置。

## 缓存设计

保留 `CacheService` 作为业务层唯一缓存入口。

目标结构：

```text
业务 Service
  -> CacheService
     -> RedisCacheService（Redis 开启）
     -> NoopCacheService（Redis 关闭）
```

规则：

- Redis 是否开启、是否可用，由缓存层处理。
- Redis 不可用时返回空缓存，主链路继续访问数据库。
- 缓存读写失败只记录 warn，不中断用户请求。
- 业务层不得直接注入 `RedisTemplate`。

建议后续新增 `NoopCacheService`，让 `RedisCacheService` 专注处理 Redis 已启用但运行异常的场景。

## 日志设计

保留 `TraceIdFilter + logback-spring.xml`：

- 每个请求生成或透传 `X-Trace-Id`。
- 日志格式固定输出 `traceId`。
- 记录请求开始、结束和耗时。
- 异步线程池通过 `TaskDecorator` 透传 MDC。

边界：

- 不记录请求体，避免敏感信息泄露。
- 默认日志级别为 `INFO`。
- 不默认接入复杂日志平台方案。

## 异常设计

异常体系保持简单：

- 保留 `BusinessException`。
- 保留 `GlobalExceptionHandler`。
- 捕获参数校验异常、业务异常、运行时异常。
- 统一返回 `Result<T>`。

建议后续给 `BusinessException` 增加 `code` 字段，默认业务错误码为 `4001`。这比在 `GlobalExceptionHandler` 中硬编码更灵活，同时避免引入复杂错误码体系。

## 测试设计

测试应覆盖模板关键路径，不把模板变成测试框架展示工程。

保留：

- `ResultTest`。

建议新增：

- `CacheService` 单元测试，验证 Redis 不可用时不影响主流程。
- `UserServiceImpl` 单元测试，覆盖缓存命中、缓存未命中、用户不存在。
- 配置类测试可后置，不默认引入 Testcontainers。

测试原则：

- 优先 JUnit5 + Mockito。
- 避免大量 `@SpringBootTest`。
- 新增 Service 方法应覆盖核心路径。

## 文档设计

文档职责分工：

- `README.md`：快速启动、核心能力、包结构、AI 协作入口。
- `ai-spec.yaml`：AI 生成代码时必须遵守的约束。
- `docs/architecture.md`：架构说明、流程图、部署拓扑。
- `docs/decisions/*.md`：关键架构决策记录。

README 应保持短，避免重复 `ai-spec.yaml` 中的规则。流程图和部署拓扑可以下沉到 `docs/architecture.md`。

`ai-spec.yaml` 应保持准确：

- 只描述当前已接入能力。
- 不写 MapStruct、OkHttp 等尚未接入技术。
- 不写过细的未来愿景。
- 保留分层边界、返回格式、缓存规则、数据库降级规则、禁止事项。

## 后续优化清单

建议按以下顺序实施：

1. 增加 `AppDatasourceProperties` 和 `AppRedisProperties`，替换散落的 `@Value`。
2. 增加 `NoopCacheService`，让 Redis 关闭时走空实现。
3. 给 `BusinessException` 增加错误码字段。
4. 增加 `CacheService` 与 `UserServiceImpl` 单元测试。
5. 将 README 中较长图表迁移到 `docs/architecture.md`。
6. 增加 `docs/decisions/0001-template-scope.md`，记录为何选择中等生产模板。

## 非目标

本阶段不做以下内容：

- 默认接入 OpenAPI。
- 默认接入 Docker 或 CI。
- 默认接入权限、审计、幂等、限流。
- 默认接入消息队列或分布式事务。
- 引入复杂错误码中心或国际化错误文案体系。

这些能力可以在业务项目需要时作为可选模块加入。
