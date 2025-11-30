# ============================
# 1. Build Stage
# ============================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /workspace

# ---- 1) 先复制所有 pom.xml 以利用 Docker 缓存（Docker 官方最佳实践） ----
COPY pom.xml .

# 复制一级模块的 pom.xml
COPY blog-framework/pom.xml ./blog-framework/pom.xml
COPY blog-framework/blog-framework-common/pom.xml ./blog-framework/blog-framework-common/pom.xml
COPY blog-framework/blog-framework-web/pom.xml ./blog-framework/blog-framework-web/pom.xml

COPY blog-module-api/pom.xml ./blog-module-api/pom.xml
COPY blog-module-auth/pom.xml ./blog-module-auth/pom.xml
COPY blog-module-biz/pom.xml ./blog-module-biz/pom.xml
COPY blog-module-publish/pom.xml ./blog-module-publish/pom.xml

# ---- 2) 下载所有依赖（只依赖 pom.xml，构建快） ----
RUN mvn -q -e -DskipTests dependency:go-offline

# ---- 3) 再复制完整源码 ----
COPY . .

# ---- 4) 构建最终可执行模块：blog-module-publish ----
RUN mvn -q -DskipTests -pl blog-module-publish -am package


# ============================
# 2. Run Stage (JRE)
# ============================
FROM eclipse-temurin:17-jre

WORKDIR /app

# 复制最终 JAR
COPY --from=builder /workspace/blog-module-publish/target/*.jar app.jar

# 新建用户组qiliblog和用户qiliblog
RUN groupadd -r qiliblog && useradd -r -g qiliblog qiliblog

# 修改权限
RUN chown -R qiliblog:qiliblog /app

USER qiliblog
EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/app.jar"]
