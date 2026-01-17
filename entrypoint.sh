#!/bin/sh

# 设置默认JVM参数
DEFAULT_JVM_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# 查找jar文件
# 使用ls命令查找jar文件，更加通用
JAR_FILE=$(ls *.jar 2>/dev/null | head -n 1)

# 如果通过ls没有找到，则尝试使用find命令
if [ -z "$JAR_FILE" ]; then
    JAR_FILE=$(find . -maxdepth 1 -name "*.jar" 2>/dev/null | head -n 1)
fi

# 检查是否找到jar文件
if [ -z "$JAR_FILE" ]; then
    echo "Error: No jar file found in current directory"
    exit 1
fi

echo "Found jar file: $JAR_FILE"

# 设置JVM参数
JVM_OPTS=${JVM_OPTS:-$DEFAULT_JVM_OPTS}

# 打印启动信息
echo "Starting application with JVM options: $JVM_OPTS"

# 构建并打印完整启动命令
STARTUP_COMMAND="java $JVM_OPTS -jar \"$JAR_FILE\""
echo "Full startup command: $STARTUP_COMMAND $@"

# 启动应用
exec java $JVM_OPTS -jar "$JAR_FILE" "$@"