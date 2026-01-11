-- 删除测试数据库
DROP DATABASE IF EXISTS qiliblog_test;
-- 创建测试数据库
CREATE DATABASE qiliblog_test;
-- 创建测试数据库用户
CREATE USER qiliblog_test WITH LOGIN PASSWORD 'qiliblog_test@1';
-- 创建测试数据库模式, 并授权给测试数据库用户
\c qiliblog_test;
CREATE SCHEMA qiliblog_test AUTHORIZATION qiliblog_test;