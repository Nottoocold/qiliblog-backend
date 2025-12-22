# ============================
# 1. Build Stage
# ============================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /build

# ---- 1) 先复制所有 pom.xml 以利用 Docker 缓存 ----
COPY pom.xml .

# 复制各个模块的 pom.xml
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

RUN groupadd -r -g 1001 qiliblog && \
    useradd -r -u 1001 -g qiliblog qiliblog

# 复制最终 JAR
COPY --from=builder --chown=qiliblog:qiliblog /build/blog-module-publish/target/*.jar ./
COPY entrypoint.sh ./

# 修改权限
RUN chown -R qiliblog:qiliblog /app && \
    chmod +x entrypoint.sh

EXPOSE 8080

USER qiliblog

# 设置入口点
ENTRYPOINT ["/app/entrypoint.sh"]