-- 反初始化脚本：清理 qiliblog 数据库及相关用户和角色
-- 请使用高权限账号（如 postgres）执行此脚本

-- 1. 切换到 postgres 数据库，确保不在目标数据库连接上
\c postgres;

-- 2. 首先处理业务账号：回收角色并删除用户
-- 撤销用户的角色关系
REVOKE qiliblog_role_readwrite FROM qiliblog_readwrite;
REVOKE qiliblog_role_readonly FROM qiliblog_readonly;
-- 删除用户
DROP USER IF EXISTS qiliblog_readwrite;
DROP USER IF EXISTS qiliblog_readonly;

-- 3. 关键步骤：撤销默认权限并删除角色
-- 连接到 qiliblog 数据库以管理其内的权限[1](@ref)
\c qiliblog;

-- 撤销由 qiliblog_owner 设置的、针对未来对象的默认权限规则[2](@ref)
ALTER DEFAULT PRIVILEGES FOR ROLE qiliblog_owner REVOKE ALL ON TABLES FROM qiliblog_role_readwrite;
ALTER DEFAULT PRIVILEGES FOR ROLE qiliblog_owner REVOKE ALL ON SEQUENCES FROM qiliblog_role_readwrite;
ALTER DEFAULT PRIVILEGES FOR ROLE qiliblog_owner REVOKE SELECT ON TABLES FROM qiliblog_role_readonly;
REVOKE ALL ON SCHEMA qiliblog FROM qiliblog_role_readwrite;
-- 谨慎操作！此命令会删除该角色在当前数据库下拥有的所有对象
DROP OWNED BY qiliblog_role_readwrite;
REVOKE ALL ON SCHEMA qiliblog FROM qiliblog_role_readonly;
-- 谨慎操作！此命令会删除该角色在当前数据库下拥有的所有对象
DROP OWNED BY qiliblog_role_readonly;

-- 删除角色（在 qiliblog 数据库连接下尝试，或切换回 postgres 后执行）
\c postgres; -- 删除角色通常可在任意数据库连接下执行，切换回 postgres 是良好实践
DROP ROLE IF EXISTS qiliblog_role_readwrite;
DROP ROLE IF EXISTS qiliblog_role_readonly;

-- 4. 清理 Schema 和项目所有者
-- 再次连接到 qiliblog 数据库以操作其下的 Schema[1](@ref)
\c qiliblog;
-- 删除 Schema 及其下的所有对象（如表、视图等）[6](@ref)
DROP SCHEMA IF EXISTS qiliblog CASCADE;

-- 切换回 postgres 数据库以删除用户
\c postgres;
-- 在删除用户 qiliblog_owner 前，需确保其已不是任何对象的所有者。
-- 如果 qiliblog_owner 是某个对象（如 Schema）的属主，需要先转移所有权或删除该对象。
-- 例如，如果 qiliblog_owner 是某个模式的所有者，需要先删除该模式（上一步的 DROP SCHEMA 已处理）或改变其所有者。
DROP USER IF EXISTS qiliblog_owner;

-- 5. 最后，删除数据库本身[7](@ref)
-- 确保没有活跃连接连接到 qiliblog 数据库[6](@ref)
SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'qiliblog'
  AND pid <> pg_backend_pid();

-- 执行删除数据库
DROP DATABASE IF EXISTS qiliblog;