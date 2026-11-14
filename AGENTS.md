# AGENTS.md

博客系统后端：Java 17 + Spring Boot 多模块 Maven 工程（版本以 pom.xml 为准）。PostgreSQL（schema `qiliblog`）+ Redis；持久层是 **io.mybatis 通用 Mapper/Service**（不是 MyBatis-Plus）；表结构由 Liquibase 管理。

## 模块与依赖方向

依赖单向：`publish → api/auth → service → framework-web → framework-common`，禁止反向引用。

| 模块 | 职责 |
|---|---|
| `blog-framework/blog-framework-common` | 纯工具类（Hutool 补充、拼音等） |
| `blog-framework/blog-framework-web` | 框架基座：ApiResult、异常、security/token、BaseEntity、DTO/VO 基类、AbstractBaseService |
| `blog-module-service/blog-module-service-main` | 博客业务：文章/分类/标签的 entity、mapper、service |
| `blog-module-service/blog-module-service-system` | 系统业务：用户/地区等 |
| `blog-module-api` | 后台管理 Controller（`api.admin`）+ 前台公开接口（`api.pub`） |
| `blog-module-auth` | 登录/注册/token 刷新，含自己的 Controller 与策略 |
| `blog-module-publish` | 启动模块 `BlogApplication` + 全部 application 配置 + Liquibase changelog |
| `blog-module-service/blog-module-service-test` | 集成测试专用模块 |

新业务逻辑放对应 service 模块；Controller 只放 `blog-module-api` / `blog-module-auth`。

## 常用命令

```bash
./mvnw -DskipTests compile                                  # 编译（测试依赖真实数据库，未起中间件时必须 skip）
docker compose -f docker-compose-middleware.yaml up -d    # 起 PostgreSQL 18 + Redis 7，首次自动建库/账号/schema
./mvnw spring-boot:run -pl blog-module-publish              # 启动，dev profile，端口 8080，Swagger: /swagger-ui.html
./mvnw test -pl blog-module-service/blog-module-service-test  # 集成测试，需先建 qiliblog_test 库（见「测试」）
./mvnw -DskipTests package                                  # 打包，产物在 blog-module-publish/target/
```

构建需 JDK 17+（`./mvnw` 用 `JAVA_HOME` 指定的 JDK）；无需预装 Maven，wrapper 已钉住 3.9.16 并自动下载。本机可用 `mvnd` 等价替换提速，其 daemon JDK 走本地 `.mvn/mvnd.properties`（含本机路径，已 gitignore，不提交）。

## 配置与环境

- 配置集中在 `blog-module-publish/src/main/resources/`：`application.yaml`（公共）+ `-dev` / `-prod`。
- 数据库双账号：`qiliblog_owner`（DDL，Liquibase 专用）、`qiliblog_readwrite`（DML，应用运行）。
- **dev 环境 Liquibase 是关闭的**（`enabled: false`），表结构变更需手动同步 dev 库或临时开启；prod 开启。

## 编码约定

- **Lombok 白名单**：只允许 `@Getter` `@Setter` `@RequiredArgsConstructor` `@Slf4j`，其余注解被 `lombok.config` 以 ERROR 禁用（用了直接编译失败）。依赖注入用 `@RequiredArgsConstructor` + `private final`。
- 注释、错误消息、Swagger 描述一律中文。
- **错误处理**：业务失败抛 `ClientException(GlobalErrorDict.XXX, "中文消息")`，由 GlobalErrorHandler 统一转 `ApiResult`；新错误码加进 `GlobalErrorDict`（400xxx 客户端 / 401xxx 认证 / 500xxx 服务端）。
- **查询**：用 io.mybatis 的 `wrapper()` / `Example` + `Fn` 方法引用，不手写 SQL；分页走 `QueryDTO` + `findPageInfo`（PageHelper）。
- URL 前缀用 `WebApiConstants`（`/api/admin`、`/api/auth`、`/api/public`）；新增免认证接口必须同步加进 `qiliblog.security.allowed-urls` 白名单。

### 新增 CRUD 实体的分层模板（参照 Tag 一族）

1. entity 继承 `BaseEntity`（自带 id/createTime/updateTime），`@Entity.Table("blog_xxx")` + `@Entity.Column` 标注（`io.mybatis.provider.Entity`）。
2. mapper 接口 `extends BaseMapper<T, Long>, Dao`（Dao 是扫描标记接口），无需实现。
3. `IXxxService extends IBaseService<T, Long>`；`XxxService extends AbstractBaseService<T, Long, XxxMapper>`，唯一性校验和业务断言写在 `beforeCreate/beforeUpdate/beforeDelete` 钩子里——CRUD 事务由基类负责，不要自行重写 CRUD。
4. DTO 三件套：`XxxCreateDTO implements CreateDTO<T>`、`XxxUpdateDTO`、`XxxQueryDTO`（转 Example）；出参 `XxxVo` + `XxxVoConvertor implements ViewVoConvertor<Entity, Vo>`（属性拷贝自动，特殊字段重写 `customize` 钩子）。
5. Controller 返回 `ApiResult.success(...)`，入参 `@Validated`，类上 SpringDoc 中文 `@Tag`、方法 `@Operation`。

参照文件：`Tag.java` / `TagMapper.java` / `TagService.java` / `TagController.java` / `TagVoConvertor.java`。

## 数据库变更

- 表结构变更**只走 Liquibase**：在 `blog-module-publish/src/main/resources/db/changelog/` 新增 changelog 文件，并在 `db.changelog-master.xml` 中 include；已发布的 changelog（如 `changelog-v1.0.0-init.xml`）不可修改。
- `db/init/` 下的 SQL/sh 仅用于 Docker 首次初始化，不是日常变更入口。

## 测试

- 集成测试统一放 `blog-module-service-test` 模块，继承 `BaseTestApp`（test profile 自动激活）；需要分类/标签基础数据则继承 `BaseBlogService`。
- 依赖真实 PostgreSQL：先执行该模块 `src/test/resources/test-init.sql` 创建 `qiliblog_test` 库/用户/schema。
- 测试类用 `@TestMethodOrder(OrderAnnotation)` + `@Order` 控制顺序，存在顺序依赖，勿单独乱序跑。
- `blog-module-publish` 下也有历史测试，同样连真实库（dev 配置）。

## Git 提交

格式 `<type>(<scope>): 中文描述`，如 `feat(scheduler): 优化文章定时发布任务性能`；type 取 feat/fix/refactor/chore/build/docs，scope 用业务或技术域名（article、docker、config…）。