# Change Log

## Lealone 5.0.1 (unreleased)

### bug fix

#### lealone database

* [Issue #22](https://github.com/lealone/Lealone-Plugins/issues/22): ReadonlyArray 在转成字符串时如果包含特殊字符错误的多加了 STRINGDECODE


### new features

#### lealone database

* 执行 create table 语句时生成的 mode l类新加 decode(String str, JsonFormat format) 方法

* create service 语句支持 parameters 子句，用于配置各类参数

* [Issue #165](https://github.com/lealone/Lealone/issues/165): 兼容 PostgreSQL 语法 ALTER TABLE tableName RENAME COLUMN oldName TO newName


#### lealone orm

* 在 model 对象和 json 字符串之间相互转换时可以自定义 json 的数据格式



