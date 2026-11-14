# Qiliblog 后端

Qiliblog 是一个博客系统后端，基于 Spring Boot 4.1.2 构建

## 🌟 功能特性

- **用户认证与授权**：基于 JWT 和 Spring Security 的安全认证体系，可轻松接入各类型认证方式
- **文章管理**：支持文章的创建、编辑、发布、分类和标签管理
- **分类与标签**：灵活的文章分类和标签系统
- **API 接口**：RESTful API 设计，便于前后端分离开发
- **分页查询**：集成 PageHelper 实现物理分页
- **邮件服务**：集成邮件发送功能
- **定时任务**：支持文章定时发布等功能

## 🛠 技术栈

- **核心框架**: Spring Boot 4.1.2
- **安全框架**: Spring Security + JWT (nimbus-jose-jwt)
- **数据持久层**: MyBatis + 通用 Mapper
- **数据库**: PostgreSQL
- **工具库**: Hutool
- **API 文档**: SpringDoc OpenAPI
- **构建工具**: Maven
- **容器化**: Docker

## 📁 项目结构

```
qiliblog-backend/
├── blog-framework/           # 基础框架模块
│   ├── blog-framework-common # 通用组件
│   └── blog-framework-web    # Web 框架组件
├── blog-module-api/          # API 接口模块
├── blog-module-auth/         # 认证授权模块
├── blog-module-service/      # 业务服务模块
│   ├── blog-module-service-main    # 主要业务逻辑
│   ├── blog-module-service-system  # 系统服务
│   └── blog-module-service-test    # 集成测试模块
├── blog-module-publish/      # 应用启动模块
├── db/init/                  # 数据库初始化脚本
├── docker-compose-middleware.yaml # 中间件一键启动（PostgreSQL + Redis）
├── Dockerfile                # Docker 构建文件
├── entrypoint.sh             # 容器启动脚本
└── pom.xml                   # 项目依赖配置
```

## 🚀 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+（或直接用仓库自带 `./mvnw`，无需预装 Maven）
- PostgreSQL 15+

### 本地开发

1. 克隆项目：

```bash
git clone https://github.com/zqqiliyc/qiliblog-backend.git
cd qiliblog-backend
```

2. 启动中间件（推荐，首次自动创建数据库/账号/schema）：

```bash
docker compose -f docker-compose-middleware.yaml up -d    # PostgreSQL 18 + Redis 7
```

也可手动创建数据库并执行 `db/init/` 目录下的 SQL 脚本。

3. 修改配置文件（使用中间件 compose 的默认账号时可跳过）：

```yaml
# blog-module-publish/src/main/resources/application-dev.yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/qiliblog
    username: your_username
    password: your_password
```

4. 启动应用：

```bash
./mvnw spring-boot:run -pl blog-module-publish
```

### Docker 部署

1. 构建 Docker 镜像：

```bash
docker build -t qiliblog-api -f Dockerfile .
```

2. 运行容器：

```bash
docker run -d -p 8080:8080 --name qiliblog qiliblog-api
```

### Docker Compose部署

1. 一键启动后端服务

```bash
docker compose up -d
```

## ⚙️ 配置说明

### 主要配置项

```yaml
qiliblog:
  token:
    style: JWT                    # token 风格
    secret: QH69T328CqtUnRM+...  # 签名密钥
    expire: 3600                  # 访问令牌过期时间(秒)
    refresh-expire: 21600         # 刷新令牌过期时间(秒)
  security:
    allowed-urls:                 # 免认证 URL 白名单
      - /swagger-ui/**
      - /api/auth/login
      - /api/public/**
```

## 🚢 部署

### 生产环境配置

生产环境配置位于 `blog-module-publish/src/main/resources/application-prod.yaml`，可根据需要调整以下参数：

- 数据库连接池配置
- 日志级别
- JWT 密钥
- 邮件服务器配置

## 🤝 贡献

欢迎提交 Issue 和 Pull Request 来改进项目。

## 📄 许可证

本项目采用 MIT 许可证。

## 📞 联系

如有问题，请通过 GitHub Issues 联系我们。