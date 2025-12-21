-- 需使用高权限账号在psql命令行中执行以下命令

-- 1. 创建数据库并切换
CREATE DATABASE "qiliblog";
-- 切换到数据库qiliblog, 或使用高权限账号重新连接到该数据库
\c
qiliblog;

-- 2. 创建资源管理账号和相关的角色，并授权
-- qiliblog_owner 是项目管理账号,此处密码仅为示例，请注意修改。
CREATE USER qiliblog_owner WITH LOGIN PASSWORD 'qiliblog@1';
CREATE ROLE qiliblog_role_readwrite;
CREATE ROLE qiliblog_role_readonly;

-- 设置: 对于qiliblog_owner 创建的表，qiliblog_role_readwrite 有 DQL（SELECT）、DML（UPDATE、INSERT、DELETE）权限。
ALTER DEFAULT PRIVILEGES FOR ROLE qiliblog_owner GRANT ALL ON TABLES TO qiliblog_role_readwrite;

-- 设置: 对于qiliblog_owner 创建的SEQUENCES，qiliblog_role_readwrite 有 DQL（SELECT）、DML（UPDATE、INSERT、DELETE）权限。
ALTER DEFAULT PRIVILEGES FOR ROLE qiliblog_owner GRANT ALL ON SEQUENCES TO qiliblog_role_readwrite;

-- 设置: 对于 qiliblog_owner 创建的表，qiliblog_role_readonly 只有 DQL（SELECT）权限。
ALTER DEFAULT PRIVILEGES FOR ROLE qiliblog_owner GRANT SELECT ON TABLES TO qiliblog_role_readonly;

-- 3. 创建业务账号，并将角色分配给业务账号
-- qiliblog_readwrite只有 DQL（SELECT）、DML（UPDATE、INSERT、DELETE）权限。
CREATE USER qiliblog_readwrite WITH LOGIN PASSWORD 'qiliblog@1';
GRANT qiliblog_role_readwrite TO qiliblog_readwrite;

-- qiliblog_readonly只有 DQL（SELECT）权限。
CREATE USER qiliblog_readonly WITH LOGIN PASSWORD 'qiliblog@1';
GRANT qiliblog_role_readonly TO qiliblog_readonly;

-- 4. 创建schema 并授权
-- schema qiliblog的owner是 qiliblog_owner账号
CREATE SCHEMA qiliblog AUTHORIZATION qiliblog_owner;

-- 授权ROLE相关SCHEMA访问权限。
GRANT USAGE ON SCHEMA qiliblog TO qiliblog_role_readwrite;
GRANT USAGE ON SCHEMA qiliblog TO qiliblog_role_readonly;
