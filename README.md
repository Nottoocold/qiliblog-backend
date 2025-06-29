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

1. 目前打包产物有两种fat-jar，Spring boot 默认打包方式，直接运行即可
```html
java -jar qiliblog-server-full.jar
```

2. 依赖jar包分离打包模式，会在blog-zpub/target下生成目录jarLibs，启动时要确保该目录与启动jar文件处于同级目录
```html
java -jar qiliblog-server.jar
```