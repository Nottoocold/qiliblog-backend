# 项目启动

**先决条件：**

1. 安装jdk17
2. 安装mysql8数据库

# 模块介绍

- blog-common 基础公共模块
- blog-domain 提供领域对象
- blog-repository 数据仓库
- blog-service 业务层
- blog-auth 认证模块
- blog-api-admin 后台管理接口模块
- blog-zpub 发布模块，也是启动模块
- blog-xxx TODO...

# 部署

1. jar包分离打包模式，会在blog-zpub/target/dist生成所有部署需要用的jar包，配置文件以及依赖
```html
java -Dloader.path=libs -Dspring.profiles.active=prod -jar qiliblog-server-${version}.jar
```