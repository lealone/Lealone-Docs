# Change Log

# Lealone 5.1.1 (unreleased)

## bug fix

#### database

* 使用 SystemSession 创建嵌入式数据库接着马上访问它会找不到数据库

* 事务因为锁超时后未正确设置 Session 状态导致无法执行某些 SQL 语句(比如 SET 语句)


## new features

#### service

* 微服务脚手架支持创建单模块微服务应用


# [Lealone 5.1.0](https://github.com/lealone/Lealone/releases/tag/lealone-5.1.0) (2022-12-24)

## bug fix

#### database

* [Issue #22](https://github.com/lealone/Lealone-Plugins/issues/22): ReadonlyArray 在转成字符串时如果包含特殊字符错误的多加了 STRINGDECODE

* 加行锁后如果在创建 redo log 之前出错会导致行锁无法释放


## new features

#### database

* 执行 create table 语句时生成的 model 类新加 decode(String str, JsonFormat format) 方法

* create service 语句支持 parameters 子句，用于配置各类参数

* [Issue #165](https://github.com/lealone/Lealone/issues/165): 兼容 PostgreSQL 语法 ALTER TABLE tableName RENAME COLUMN oldName TO newName

* 支持 set、list、map 数据类型

* 执行 create service 语句时可以通过参数配置是否生成 MAP 和 JSON 对应的 EXECUTOR METHOD

* 支持 GRANT/REVOKE EXECUTE ON SERVICE 语句

* 执行 create table 语句时可以通过 DAO_NAME 参数设置 dao 字段的名称


#### orm

* 在 model 对象和 json 字符串之间相互转换时可以自定义 json 的数据格式

* 支持 left join 和 right join


# [Lealone 5.0.0](https://github.com/lealone/Lealone/releases/tag/lealone-5.0.0) (2022-10-24)

这是10年以来 Lealone 发布的第一个 GA 版本！

官方文档: http://lealone.org 或 https://github.com/lealone/Lealone-Docs

