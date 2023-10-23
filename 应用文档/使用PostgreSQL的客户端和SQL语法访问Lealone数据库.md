Lealone 高度兼容 PostgreSQL 的协议和 SQL 语法，可以使用 PostgreSQL 的各种客户端访问 Lealone。

## 运行需要

* [JDK 1.8+](https://www.oracle.com/java/technologies/downloads/)

* PostgreSQL 版本支持 9.x 和 14.x 系列


## 下载 Lealone

[lealone-6.0.0-SNAPSHOT.jar](https://github.com/lealone/Lealone-Docs/releases/download/lealone-docs-1.0.0/lealone-6.0.0-SNAPSHOT.jar)

Lealone 只有一个 jar 包，下载下来之后随意放到一个目录即可

也可以从源代码构建最新版本，请阅读文档: [从源码构建 Lealone](https://github.com/lealone/Lealone-Docs/blob/master/%E5%BA%94%E7%94%A8%E6%96%87%E6%A1%A3/%E4%BB%8E%E6%BA%90%E7%A0%81%E6%9E%84%E5%BB%BALealone.md)


## 启动 Lealone 数据库

打开一个新的命令行窗口，运行: `java -jar lealone-6.0.0-SNAPSHOT.jar`

```java
Lealone version: 6.0.0-SNAPSHOT
Use default config
Base dir: E:/lealone/target/lealone_data
Init storage engines: 6 ms
Init transaction engines: 31 ms
Init sql engines: 3 ms
Init protocol server engines: 99 ms
Init lealone database: 93 ms
TcpServer started, host: 127.0.0.1, port: 9210
MySQLServer started, host: 127.0.0.1, port: 3306
MongoServer started, host: 127.0.0.1, port: 27017
PgServer started, host: 127.0.0.1, port: 5432
Total time: 288 ms (Load config: 1 ms, Init: 238 ms, Start: 49 ms)
Exit with Ctrl+C
```

要停止 Lealone，直接按 Ctrl + C


## 用 PostgreSQL 客户端访问 Lealone 数据库

执行以下命令启动 PostgreSQL 客户端:

`psql -h 127.0.0.1 -p 5432 -U postgres -W`

提示口令时输入: postgres

```sql
口令:
psql (14.0, 服务器 8.2.23)
输入 "help" 来获取帮助信息.

postgres=> create table if not exists pet(name varchar(20), age int);
UPDATE 0
postgres=> insert into pet values('pet1', 2);
CommandInterfaceINSERT 0 1
postgres=> select count(*) from pet;
 count(*)
----------
        1
(1 行记录)

postgres=>
```
