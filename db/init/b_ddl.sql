USE `qiliblog`;

DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role
(
    id          BIGINT PRIMARY KEY COMMENT '主键ID',
    code        VARCHAR(255) NOT NULL COMMENT '角色编码',
    name        VARCHAR(255) NOT NULL COMMENT '角色名称',
    sort        INT                   DEFAULT 0 COMMENT '排序',
    state       INT          NOT NULL DEFAULT 0 COMMENT '状态 0-启用 1-禁用',
    remark      TEXT COMMENT '备注',
    create_time TIMESTAMP             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag    INT                   DEFAULT 0 COMMENT '删除标志 0:未删除 1:已删除',
    CONSTRAINT UNIQUE (del_flag, code)
) COMMENT ='角色表';

DROP TABLE IF EXISTS sys_permission;
CREATE TABLE sys_permission
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    code        VARCHAR(255) NOT NULL COMMENT '权限标识',
    name        VARCHAR(255) NOT NULL COMMENT '权限名称',
    parent_id   BIGINT       NOT NULL COMMENT '父级权限ID, 0表示顶级权限',
    sort        INT       DEFAULT 0 COMMENT '排序',
    state       INT       DEFAULT 0 COMMENT '状态 0:正常 1:禁用',
    remark      TEXT COMMENT '备注',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT UNIQUE (code)
) COMMENT ='权限表';

DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user
(
    id          BIGINT PRIMARY KEY COMMENT '主键ID',
    username    VARCHAR(255) NOT NULL COMMENT '用户名 登录名',
    password    VARCHAR(255) NOT NULL COMMENT '密码 加密',
    nickname    VARCHAR(255) COMMENT '昵称',
    state       INT       DEFAULT 0 COMMENT '状态 0:正常 1:禁用',
    email       VARCHAR(255) COMMENT '邮箱',
    phone       VARCHAR(20) COMMENT '手机号',
    avatar      VARCHAR(255) COMMENT '头像URL',
    dept_id     BIGINT    DEFAULT -1 COMMENT '部门ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag    INT       DEFAULT 0 COMMENT '删除标志 0:未删除 1:已删除',
    CONSTRAINT UNIQUE (del_flag, username),
    CONSTRAINT UNIQUE (del_flag, email),
    CONSTRAINT UNIQUE (del_flag, phone)
) COMMENT ='系统用户表';

DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role
(
    id          BIGINT PRIMARY KEY COMMENT '主键ID',
    user_id     BIGINT NOT NULL COMMENT '用户ID',
    role_id     BIGINT NOT NULL COMMENT '角色ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT UNIQUE (user_id, role_id)
) COMMENT ='用户角色关联表';

DROP TABLE IF EXISTS sys_role_permission;
CREATE TABLE sys_role_permission
(
    id            BIGINT PRIMARY KEY COMMENT '主键ID',
    role_id       BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    create_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT UNIQUE (role_id, permission_id)
) COMMENT ='角色权限关联表';

DROP TABLE IF EXISTS `category`;
-- 创建分类表 (`category`)
CREATE TABLE `category`
(
    `id`          BIGINT PRIMARY KEY COMMENT '主键',
    `name`        VARCHAR(64) NOT NULL COMMENT '分类名称 (必须唯一，如 "技术", "生活")',
    `slug`        VARCHAR(64) NOT NULL COMMENT '分类 URL 友好标识符 (必须唯一，如 "tech", "life")',
    `post_count` INT NOT NULL DEFAULT 0 COMMENT '文章数, 用于统计分类下文章数',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '分类描述',
    `create_time` TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    UNIQUE KEY `idx_name` (`name`),
    UNIQUE KEY `idx_slug` (`slug`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT ='文章分类表';

DROP TABLE IF EXISTS `tags`;
-- 创建标签表 (`tags`)
CREATE TABLE `tags`
(
    `id`          BIGINT PRIMARY KEY COMMENT '标签唯一标识符 (主键)',
    `name`        VARCHAR(64) NOT NULL COMMENT '标签名称 (必须唯一，如 "Java", "Spring Boot")',
    `slug`        VARCHAR(64) NOT NULL COMMENT '标签 URL 友好标识符 (必须唯一，如 "java", "spring-boot")',
    `post_count` INT NOT NULL DEFAULT 0 COMMENT '文章数, 用于统计标签下文章数',
    `description` VARCHAR(255)         DEFAULT NULL COMMENT '标签描述',
    `create_time` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    UNIQUE KEY `idx_tag_name` (`name`),
    UNIQUE KEY `idx_tag_slug` (`slug`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT ='文章标签表';

DROP TABLE IF EXISTS `article`;
-- 创建文章表 (`article`)
CREATE TABLE `article`
(
    `id`             BIGINT PRIMARY KEY COMMENT '文章唯一标识符 (主键)',
    `title`          VARCHAR(255) NOT NULL COMMENT '文章标题',
    `slug`           VARCHAR(255) NOT NULL COMMENT '文章 URL 友好标识符 (必须唯一，用于生成永久链接)',
    `content`        LONGTEXT     NOT NULL COMMENT '文章详细内容 (支持 Markdown/HTML)',
    `summary`        TEXT                  DEFAULT NULL COMMENT '文章摘要 (用于列表页预览)',
    `cover_image`    VARCHAR(255)          DEFAULT NULL COMMENT '文章封面图片 URL',
    `status`         TINYINT        DEFAULT 0 COMMENT '文章状态: draft(0草稿), published(1已发布), private(2私密)',
    `category_id`    BIGINT    NOT NULL COMMENT '外键，关联分类表 id (文章所属主分类)',
    `author_id`      BIGINT       NOT NULL COMMENT '外键，关联用户表 id (文章作者)',
    `is_top`         TINYINT        DEFAULT 0 COMMENT '是否置顶: 1(是), 0(否)',
    `is_recommend`   TINYINT        DEFAULT 0 COMMENT '是否推荐: 1(是), 0(否)',
    `is_commentable` TINYINT        DEFAULT 1 COMMENT '是否允许评论: 1(允许), 0(不允许)',
    `publish_at`     TIMESTAMP NULL DEFAULT NULL COMMENT '文章需要定时发布时设置',
    `published_time` TIMESTAMP    NULL     DEFAULT NULL COMMENT '文章发布时间 (当状态变为 published 时设置)',
    `modified_time`  TIMESTAMP    NULL     DEFAULT NULL COMMENT '文章内容修改时间(当状态为 published 时, 每次修改时设置)',
    `view_count`     INT                   DEFAULT 0 COMMENT '文章阅读次数',
    `like_count`     INT                   DEFAULT 0 COMMENT '文章点赞次数',
    `word_count`     INT                   DEFAULT 0 COMMENT '文章字数统计',
    `read_time`      VARCHAR(64)           DEFAULT NULL COMMENT '预计阅读时间',
    `create_time`    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `update_time`    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
    UNIQUE KEY `idx_slug` (`slug`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT ='文章核心信息表';

DROP TABLE IF EXISTS `rel_article_tag`;
-- 创建文章标签关联表 (`rel_article_tag`)
CREATE TABLE `rel_article_tag`
(
    `id`          BIGINT PRIMARY KEY COMMENT '主键ID',
    `article_id`  BIGINT NOT NULL COMMENT '文章ID',
    `tag_id`      BIGINT NOT NULL COMMENT '标签ID',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT ='文章标签关联表';

DROP TABLE IF EXISTS `sys_token`;
CREATE TABLE `sys_token`
(
    `id`                 bigint               NOT NULL COMMENT '主键ID',
    `access_token`       varchar(512)         NOT NULL COMMENT '访问令牌字符串',
    `refresh_token`      varchar(512) COMMENT '刷新令牌字符串',
    `token_style`        enum ('JWT', 'UUID') NOT NULL COMMENT '令牌风格',
    `user_id`            bigint               NOT NULL COMMENT '关联用户ID',
    `issued_at`          datetime             NOT NULL COMMENT '颁发时间',
    `expired_at`         datetime             NOT NULL COMMENT '过期时间',
    `refresh_expired_at` datetime                      DEFAULT NULL COMMENT '刷新令牌过期时间',
    `revoked`            tinyint(1)                    DEFAULT '0' COMMENT '是否已撤销（1-是，0-否）',
    `revoked_at`         datetime                      DEFAULT NULL COMMENT '撤销时间',
    `ip_address`         varchar(45)                   DEFAULT NULL COMMENT '客户端IP',
    `additional_info`    json                          DEFAULT NULL COMMENT '扩展信息（JSON格式）',
    `create_time`        datetime             NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `update_time`        datetime             NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_access_token` (`access_token`(255)),
    UNIQUE KEY `uk_refresh_token` (`refresh_token`(255)),
    KEY `idx_user_status` (`user_id`, `revoked`, `expired_at`),
    KEY `idx_refresh_expiry` (`refresh_expired_at`, `revoked`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统令牌表';

DROP TABLE IF EXISTS `sys_region`;
CREATE TABLE `sys_region`
(
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `code` VARCHAR(32) NOT NULL COMMENT '编码',
    `name` VARCHAR(64) NOT NULL COMMENT '名称',
    `pcode` VARCHAR(32) NULL COMMENT '父级编码',
    `level` INT NOT NULL COMMENT '区域类型;1-省级,2-市级,3-区县级,4-乡镇级,5-村级'
) COMMENT ='系统区域表' AUTO_INCREMENT = 1;
ALTER TABLE `sys_region` ADD UNIQUE (`code`);
