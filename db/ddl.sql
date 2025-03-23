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
    CONSTRAINT UNIQUE (code, del_flag)
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
    del_flag    INT       DEFAULT 0 COMMENT '删除标志 0:未删除 1:已删除',
    CONSTRAINT UNIQUE (code, del_flag)
) COMMENT ='权限表';

DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user
(
    id          BIGINT PRIMARY KEY COMMENT '主键ID',
    username    VARCHAR(255) NOT NULL COMMENT '用户名 登录名',
    password    VARCHAR(255) NOT NULL COMMENT '密码 sha256加密',
    nickname    VARCHAR(255) COMMENT '昵称',
    state       INT       DEFAULT 0 COMMENT '状态 0:正常 1:禁用',
    email       VARCHAR(255) COMMENT '邮箱',
    phone       VARCHAR(20) COMMENT '手机号',
    avatar      VARCHAR(255) COMMENT '头像URL',
    dept_id     BIGINT    DEFAULT -1 COMMENT '部门ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag    INT       DEFAULT 0 COMMENT '删除标志 0:未删除 1:已删除',
    CONSTRAINT UNIQUE (username, del_flag),
    CONSTRAINT UNIQUE (email, del_flag),
    CONSTRAINT UNIQUE (phone, del_flag)
) COMMENT ='系统用户表';

DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role
(
    id          BIGINT PRIMARY KEY COMMENT '主键ID',
    user_id     BIGINT NOT NULL COMMENT '用户ID',
    role_id     BIGINT NOT NULL COMMENT '角色ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag    INT       DEFAULT 0 COMMENT '删除标志 0:未删除 1:已删除',
    CONSTRAINT UNIQUE (user_id, role_id, del_flag)
) COMMENT ='用户角色关联表';

DROP TABLE IF EXISTS sys_role_permission;
CREATE TABLE sys_role_permission
(
    id            BIGINT PRIMARY KEY COMMENT '主键ID',
    role_id       BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    create_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag      INT       DEFAULT 0 COMMENT '删除标志 0:未删除 1:已删除',
    CONSTRAINT UNIQUE (role_id, permission_id, del_flag)
) COMMENT ='角色权限关联表';
