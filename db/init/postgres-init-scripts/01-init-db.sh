#!/usr/bin/env bash
set -e

# superuser创建数据库
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE qiliblog;
EOSQL

# 切换到新数据库并创建用户和schema
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "qiliblog" <<-EOSQL
    -- 创建资源管理账号和相关的角色
    CREATE USER qiliblog_owner WITH LOGIN PASSWORD 'qiliblog@1';
    CREATE ROLE qiliblog_role_readwrite;
    
    -- 设置: 对于qiliblog_owner 创建的表，qiliblog_role_readwrite 有 DQL（SELECT）、DML（UPDATE、INSERT、DELETE）权限。
    ALTER DEFAULT PRIVILEGES FOR ROLE qiliblog_owner GRANT ALL ON TABLES TO qiliblog_role_readwrite;

    -- 设置: 对于qiliblog_owner 创建的SEQUENCES，qiliblog_role_readwrite 有 DQL（SELECT）、DML（UPDATE、INSERT、DELETE）权限。
    ALTER DEFAULT PRIVILEGES FOR ROLE qiliblog_owner GRANT ALL ON SEQUENCES TO qiliblog_role_readwrite;

    -- 创建业务账号，并将角色分配给业务账号
    -- qiliblog_readwrite只有 DQL（SELECT）、DML（UPDATE、INSERT、DELETE）权限。
    CREATE USER qiliblog_readwrite WITH LOGIN PASSWORD 'qiliblog@1';
    GRANT qiliblog_role_readwrite TO qiliblog_readwrite;

    -- 创建schema 并授权
    -- schema qiliblog的owner是 qiliblog_owner账号
    CREATE SCHEMA qiliblog AUTHORIZATION qiliblog_owner;
    
    -- 授予schema访问权限
    GRANT USAGE ON SCHEMA qiliblog TO qiliblog_role_readwrite;
EOSQL

echo "Database, users, roles, and schema init successfully!"