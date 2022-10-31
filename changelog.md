# Change Log

## Lealone 5.0.1 (unreleased)

### bug fix

#### database

* [Issue #22](https://github.com/lealone/Lealone-Plugins/issues/22): ReadonlyArray 在转成字符串时如果包含特殊字符错误的多加了 STRINGDECODE


### new features

#### database

* 执行 create table 语句时生成的 model 类新加 decode(String str, JsonFormat format) 方法

* create service 语句支持 parameters 子句，用于配置各类参数

* [Issue #165](https://github.com/lealone/Lealone/issues/165): 兼容 PostgreSQL 语法 ALTER TABLE tableName RENAME COLUMN oldName TO newName


#### orm

* 在 model 对象和 json 字符串之间相互转换时可以自定义 json 的数据格式


## Lealone 5.0.0 (2022-10-24)

这是10年以来 Lealone 发布的第一个 GA 版本！

官方文档: http://lealone.org 或 https://github.com/lealone/Lealone-Docs

