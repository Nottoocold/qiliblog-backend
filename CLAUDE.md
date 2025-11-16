# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

QiliBlog 是一个基于 Spring Boot 3.4.4、Java 17 和 MyBatis 的博客系统后端。项目采用多模块 Maven
架构，遵循分层设计模式，提供完整的博客内容管理和用户认证授权功能。

## 技术栈

- **基础框架**：Spring Boot 3.4.4 + Java 17
- **数据访问**：MyBatis 3.0.4 + MyBatis-Mapper 2.2.3（通用 Mapper）
- **数据库**：MySQL 8（HikariCP 连接池）
- **权限认证**：Spring Security + JWT（nimbus-jose-jwt 10.5）
- **API 文档**：Swagger/OpenAPI 3（springdoc 2.8.13）
- **工具库**：Hutool 5.8.26、Lombok、PageHelper 6.1.0

## 项目结构

项目采用六模块分层架构：

```
qiliblog-backend/
├── blog-zpub/                    # 发布模块（启动入口，端口 8080）
├── blog-framework/               # 基础框架（父模块）
│   ├── blog-framework-common     # 通用工具类、常量、枚举
│   └── blog-framework-web        # Web 基础设施（JWT、Spring Security、响应封装）
├── blog-biz-core/                # 核心业务逻辑（Entity、DTO、VO、Service、Mapper）
├── blog-auth/                    # 认证授权模块（登录、注册、Token 刷新）
├── blog-api-admin/               # 后台管理 API（需要认证的接口）
└── blog-api-public/              # 公开访问 API（无需认证的接口）
```

### 模块依赖关系

```
blog-zpub (启动模块)
  ├─ blog-api-admin
  │   └─ blog-biz-core
  │       └─ blog-framework-web
  │           └─ blog-framework-common
  ├─ blog-api-public
  │   └─ blog-biz-core
  └─ blog-auth
      └─ blog-biz-core
```

- **blog-framework-common**：最底层，提供通用工具类，无业务依赖
- **blog-framework-web**：依赖 common，提供 Web 层基础设施
- **blog-biz-core**：依赖 framework-web，包含所有业务逻辑和数据访问
- **blog-api-admin/public**：依赖 biz-core，提供 RESTful API 端点
- **blog-auth**：依赖 biz-core，处理认证授权逻辑
- **blog-zpub**：聚合所有模块，作为应用启动入口

## 核心架构模式

### 1. 分层架构

- **Entity**：继承 `BaseEntity`（含 id、createTime、updateTime）或 `BaseEntityWithDel`（扩展 delFlag 逻辑删除）
- **DTO**：数据传输对象，分为 CreateDTO、UpdateDTO、QueryDTO
- **VO**：视图对象，用于前端展示
- **Service**：业务逻辑层，继承 `IBaseService` 获得通用 CRUD 能力
- **Mapper**：数据访问层，继承 `BaseMapper<Entity, Long>`（通用 Mapper）
- **Controller**：RESTful API 层

### 2. 数据库表命名规范

- 业务表：`blog_`前缀（如 `blog_article`、`blog_category`）
- 系统表：`sys_`前缀（如 `sys_user`、`sys_role`）
- 关联表：`rel_`前缀（如 `rel_article_tag`）

### 3. 关键设计决策

- **通用 Mapper**：使用 MyBatis-Mapper 2.2.3，基本无需手写 SQL，自动生成 CRUD 方法
- **逻辑删除**：继承 `BaseEntityWithDel` 的实体支持逻辑删除（`delFlag` 字段），查询自动过滤
- **JWT 无状态认证**：服务端不保存会话，Token 包含在 Authorization 请求头
- **模块解耦**：API 层（admin/public）与业务层（biz-core）分离，职责清晰
- **依赖注入**：使用构造函数注入（`@RequiredArgsConstructor`）而非字段注入
- **参数校验**：使用 `@Validated` 和 JSR-303 注解进行参数校验

### 4. 代码示例

**Entity 示例**（参考 `blog-biz-core/entity/Tag.java`）：

```java
@Entity.Table("blog_tag")
public class Tag extends BaseEntityWithDel {
    @Entity.Column(value = "name", remark = "标签名称")
    private String name;

    @Entity.Column(value = "slug", remark = "URL 友好标识符")
    private String slug;
}
```

**Mapper 示例**（参考 `CategoryMapper.java`）：

```java
public interface CategoryMapper extends BaseMapper<Category, Long>, Dao {
    // 基本 CRUD 方法由 BaseMapper 自动提供，无需手写
}
```

**Service 实现示例**（参考 `TagService.java`）：

```java
@Service
@Transactional(rollbackFor = Exception.class)
public class TagService extends AbstractBaseService<Tag, Long, TagMapper>
    implements ITagService {

    @Override
    public Tag create(CreateDTO<Tag> dto) {
        Tag entity = dto.toEntity();
        // 使用 validateBeforeCreateWithDB 检查唯一性
        validateBeforeCreateWithDB(Map.of(Tag::getName, entity.getName()),
            "标签名称已存在");
        baseMapper.insert(entity);
        return entity;
    }
}
```

**Controller 示例**（参考 `ArticleController.java`）：

```java
@Tag(name = "文章接口")
@RestController
@RequestMapping(WebApiConstants.API_ADMIN_PREFIX + "/post")
@RequiredArgsConstructor
public class ArticleController {
    private final IArticleService articleService;

    @Operation(summary = "分页查询文章")
    @GetMapping("/page")
    public ApiResult<PageResult<ArticleVo>> pageQuery(ArticleQueryDTO queryDto) {
        PageResult<Article> pageInfo = articleService.findPageInfo(queryDto);
        return ApiResult.success(pageInfo.map(convertor::toViewVoList));
    }
}

## 开发命令

### 构建和运行

```bash
# 编译
mvn clean compile

# 打包（生成可执行 JAR，输出在 blog-zpub/target/）
mvn clean package

# 运行测试
mvn test

# 跳过测试打包
mvn clean package -DskipTests

# 启动应用（开发环境，默认端口 8080）
java -Dspring.profiles.active=dev -jar blog-zpub/target/qiliblog-server-0.0.1.jar

# 启动应用（生产环境，使用外部依赖）
java -Dloader.path=libs -Dspring.profiles.active=prod -jar qiliblog-server-0.0.1.jar
```

### 数据库配置

主配置文件：`blog-zpub/src/main/resources/application.yaml`

数据库连接配置在环境特定的配置文件中（`application-dev.yaml` 或 `application-prod.yaml`）。

数据库初始化脚本位于：`db/init/`

- `a_create_db_and_user.sql`：创建数据库和用户
- `b_ddl.sql`：数据表结构定义

## 核心业务流程

### 文章发布流程

1. 用户登录获取 JWT Token（`POST /api/auth/login`）
2. 创建草稿（`POST /api/admin/post/draft`），status=0
3. 编辑文章
4. 发布文章（status 更新为 1），系统自动更新统计字段
5. 前端通过公开 API 查询已发布文章（`/api/public/**`）

### 新增功能模块步骤

1. **创建 Entity**（`blog-biz-core/entity/`）
    - 继承 `BaseEntity`（含 id、createTime、updateTime）或 `BaseEntityWithDel`（增加逻辑删除字段）
    - 使用 `@io.mybatis.provider.Entity.Column` 注解标注字段映射

2. **创建 DTO**（`blog-biz-core/dto/`）
    - 创建 `CreateDTO<Entity>` 用于创建操作，实现 `toEntity()` 方法
    - 创建 `UpdateDTO<Entity>` 用于更新操作，实现 `fillEntity()` 方法
    - 创建 `QueryDTO<Entity>` 用于查询操作，继承自 `QueryDTO<T>` 实现 `toExample()` 方法

3. **创建 VO**（`blog-biz-core/vo/`，可选）
    - 用于前端展示的视图对象
    - 创建对应的 Convertor 进行实体与 VO 之间的转换

4. **创建 Mapper**（`blog-biz-core/repository/mapper/`）
    - 继承 `BaseMapper<Entity, Long>` 和 `Dao` 接口
    - 基本 CRUD 操作无需编写 SQL，由通用 Mapper 自动生成

5. **创建 Service**（`blog-biz-core/service/`）
    - 接口继承 `IBaseService<Entity, Long>`
    - 实现类继承 `AbstractBaseService<Entity, Long, Mapper>` 并实现接口
    - 重写 `create()` 和 `update()` 方法实现业务逻辑
    - 使用 `validateBeforeCreateWithDB()` 和 `validateBeforeUpdateWithDB()` 进行数据库唯一性校验

6. **创建 Controller**（`blog-api-admin/` 或 `blog-api-public/`）
    - 使用 `@RestController` 和 `@RequestMapping` 注解
    - 管理后台接口使用 `WebApiConstants.API_ADMIN_PREFIX`（`/api/admin`）
    - 公开接口使用 `WebApiConstants.API_PUBLIC_PREFIX`（`/api/public`）
    - 返回值使用 `ApiResult<T>` 包装
    - 使用 Swagger 注解（`@Tag`、`@Operation`）生成 API 文档

7. **配置权限**（如需保护接口）
    - 在 `SpringSecurityConfig` 中配置 URL 访问规则
    - 或在 `application.yaml` 的 `qiliblog.security.allowed-urls` 中添加公开 URL

### 通用 Service 提供的方法

继承 `IBaseService<Entity, Long>` 后自动获得：

- `findById(Long id)`：根据 ID 查询
- `findOne(Example<T> example)`：条件查询单条记录
- `findList(QueryDTO<T> queryDto)`：条件查询列表
- `findList(Example<T> example)`：使用 Example 查询列表
- `findPageInfo(QueryDTO<T> queryDto)`：分页查询，返回 `PageResult<T>`
- `findByFieldList(Fn<T, F> field, Collection<F> values)`：根据字段值列表查询
- `count(Example<T> example)`：统计记录数
- `create(CreateDTO<T> dto)`：创建记录（需重写实现业务逻辑）
- `update(UpdateDTO<T> dto)`：更新记录（需重写实现业务逻辑）
- `deleteById(Long id)`：删除记录（逻辑删除）
- `deleteByFieldList(Fn<T, F> field, Collection<F> values)`：批量删除

## 配置文件位置

- **主配置**：`blog-zpub/src/main/resources/application.yaml`
    - MyBatis、Jackson、日志等通用配置
    - Token 配置（`qiliblog.token`）
    - 安全配置（`qiliblog.security.allowed-urls`）
- **开发环境**：`blog-zpub/src/main/resources/application-dev.yaml`（数据库连接等）
- **生产环境**：`blog-zpub/src/main/resources/application-prod.yaml`（数据库连接等）

## 重要注意事项

### 认证与安全

- 所有密码使用 BCrypt 加密存储
- JWT Access Token 默认过期时间 1 小时（3600 秒）
- JWT Refresh Token 默认过期时间 6 小时（21600 秒）
- Token 配置在 `application.yaml` 的 `qiliblog.token` 节点
- 需要认证的请求在 Header 中添加 `Authorization: Bearer {token}`

### 逻辑删除

- 继承 `BaseEntityWithDel` 的实体支持逻辑删除
- 使用 `@LogicalColumn(delete = "1")` 注解标识删除字段
- `delFlag` 为 0 表示未删除，1 表示已删除
- 查询操作自动过滤已删除数据

### API 设计

- 管理后台接口路径：`/api/admin/**`（需要认证）
- 公开访问接口路径：`/api/public/**`（无需认证）
- 认证接口路径：`/api/auth/**`（登录、注册、刷新 Token）
- API 统一返回格式：`ApiResult<T>`，包含 code、message、data 字段
- 分页查询返回 `PageResult<T>`，包含 list、total、pageNum、pageSize 等字段

### 开发工具

- Swagger API 文档访问地址：`http://localhost:8080/swagger-ui.html`
- 邮件验证功能在 `application.yaml` 中配置（`qiliblog.verification.enabled`）
- 日志文件位置：`./log/qiliblog-backend.log`

### MyBatis 配置

- 自动驼峰命名转换已开启（`map-underscore-to-camel-case: true`）
- 默认语句超时时间：10 秒
- 使用通用 Mapper，大部分情况无需编写 XML 映射文件
