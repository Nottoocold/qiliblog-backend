-- 创建测试账号
CREATE
USER qiliblog_tester WITH LOGIN PASSWORD 'qiliblog@1';
GRANT qiliblog_role_readwrite TO qiliblog_tester;

-- 创建测试schema
CREATE SCHEMA qiliblog_test AUTHORIZATION qiliblog_owner;

-- 授权ROLE相关SCHEMA访问权限。
GRANT
USAGE
ON
SCHEMA
qiliblog_test TO qiliblog_role_readwrite;
GRANT USAGE ON SCHEMA
qiliblog_test TO qiliblog_role_readonly;