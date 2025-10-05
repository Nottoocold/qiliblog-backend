# 项目启动

**先决条件：**

1. 安装jdk17
2. 安装mysql8数据库

# 模块介绍

- blog-framework 基础框架
- blog-biz-core 核心业务
- blog-auth 认证模块
- blog-api-admin 后台管理接口
- blog-api-public 公开接口
- blog-zpub 发布模块，也是启动模块

# 部署

1. jar包分离打包模式，会在blog-zpub/target/dist生成所有部署需要用的jar包，配置文件以及依赖
```html
java -Dloader.path=libs -Dspring.profiles.active=prod -jar qiliblog-server-${version}.jar
```