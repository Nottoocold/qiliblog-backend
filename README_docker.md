# 运行 Docker 容器时调整时区

在Linux系统中，控制时区和时间的主要是两个地方：

- /etc/localtime，主要代表当前时区设置，一般链接指向/usr/share/zoneinfo目录下的具体时区。
- /etc/timezone，主要代表当前时区设置下的本地时间。

## 1. 通用 docker 时区修改方案

1. 宿主机为Linux系统，可以直接将宿主机上的/etc/timezone和/etc/localtime挂载到容器中，这样可以保持容器和宿主机时区和时间一致。

```shell
 -v /etc/timezone:/etc/timezone:ro -v /etc/localtime:/etc/localtime:ro
```

2. 通过传递环境变量改变容器时区

- **适用于基于 Debian 基础镜像, CentOS 基础镜像 制作的 Docker 镜像**
- **不适用于基于 Alpine 基础镜像, Ubuntu 基础镜像 制作的 Docker 镜像**

对于基于 Debian 基础镜像，CentOS 基础镜像制作的 Docker 镜像，在运行 Docker 容器时，传递环境变量-e TZ=Asia/Shanghai进去，能修改
docker 容器时区。

```shell
-e TZ=Asia/Shanghai
```

## 2. 制作 Docker 镜像时调整时区

通过编写 Dockerfile,构建自己的 Docker 镜像，可以永久解决时区问题。

1. Alpine 根据[《Setting the timezone》](https://wiki.alpinelinux.org/wiki/Setting_the_timezone)提示，我们可以将以下代码添加到
   Dockerfile 中：

```shell
ENV TZ Asia/Shanghai

RUN apk add tzdata && cp /usr/share/zoneinfo/${TZ} /etc/localtime \
    && echo ${TZ} > /etc/timezone \
    && apk del tzdata
```

2. Debian 基础镜像 中已经安装了 tzdata 包，我们可以将以下代码添加到 Dockerfile 中：

```shell
ENV TZ=Asia/Shanghai \
    DEBIAN_FRONTEND=noninteractive

RUN ln -fs /usr/share/zoneinfo/${TZ} /etc/localtime \
    && echo ${TZ} > /etc/timezone \
    && dpkg-reconfigure --frontend noninteractive tzdata \
    && rm -rf /var/lib/apt/lists/*
```

3. Ubuntu 基础镜像中没有安装了 tzdata 包，因此我们需要先安装 tzdata 包。

```shell
ENV TZ=Asia/Shanghai \
    DEBIAN_FRONTEND=noninteractive

RUN apt update \
    && apt install -y tzdata \
    && ln -fs /usr/share/zoneinfo/${TZ} /etc/localtime \
    && echo ${TZ} > /etc/timezone \
    && dpkg-reconfigure --frontend noninteractive tzdata \
    && rm -rf /var/lib/apt/lists/*
```

4. CentOS 镜像中已经安装了 tzdata 包，我们可以将以下代码添加到 Dockerfile 中：

```shell
ENV TZ Asia/Shanghai

RUN ln -fs /usr/share/zoneinfo/${TZ} /etc/localtime \
    && echo ${TZ} > /etc/timezone
```
