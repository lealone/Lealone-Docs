Lealone 高度兼容 MySQL 的协议和 SQL 语法，可以使用 MySQL 的各种客户端访问 Lealone。

## 运行需要

* [JDK 1.8+](https://www.oracle.com/java/technologies/downloads/)

* MySQL 协议版本支持 5.x 到 8.x 系列


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
Total time: 288 ms (Load config: 1 ms, Init: 238 ms, Start: 49 ms)
Exit with Ctrl+C
```

要停止 Lealone，直接按 Ctrl + C


## 用 MySQL 客户端访问 Lealone 数据库

执行以下命令启动 MySQL 客户端:

`mysql --no-beep -h localhost -P 3306 -u root`

```sql
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 0
Server version: 5.1.48-lealone-5.2.0

Copyright (c) 2000, 2013, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> create table if not exists pet(name varchar(20), age int);
Query OK, 0 rows affected (0.00 sec)

mysql> insert into pet values('pet1', 2);
Query OK, 1 row affected (0.01 sec)

mysql> select count(*) from pet;
+----------+
| COUNT(*) |
+----------+
|        1 |
+----------+
1 row in set (0.01 sec)

mysql>
```
