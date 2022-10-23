## 运行需要

* JDK 1.8+

* MySQL 协议版本支持 5.x 到 8.x 系列

* PostgreSQL JDBC Driver 支持 9.x 和 42.x 两个系列版本


## 下载 Lealone-xSQL

[lealone-xsql-5.0.0.jar](https://github.com/lealone/Lealone-Plugins/releases/download/lealone-plugins-5.0.0/lealone-xsql-5.0.0.jar)

Lealone-xSQL 只有一个 jar 包，下载下来之后随意放到一个目录即可

也可以[从源代码构建最新版本](https://github.com/lealone/Lealone-Plugins/tree/master/xsql)


## 启动 Lealone-xSQL

打开一个新的命令行窗口，运行: `java -jar lealone-xsql-5.0.0.jar`

```java
Lealone version: 5.0.0
Loading config from jar:file:/E:/lealone-xsql/lealone-xsql-5.0.0.jar!/lealone.yaml
Base dir: .\lealone_data
Init storage engines: 3 ms
Init transaction engines: 60 ms
Init sql engines: 3 ms
Init protocol server engines: 171 ms
Init lealone database: 0 ms
Starting tcp server accepter
TcpServer started, host: 127.0.0.1, port: 9210
Starting tcp server accepter
MySQLServer started, host: 127.0.0.1, port: 9310
Starting tcp server accepter
PgServer started, host: 127.0.0.1, port: 9510
Total time: 326 ms (Load config: 76 ms, Init: 242 ms, Start: 8 ms)
Exit with Ctrl+C
```

要停止 Lealone-xSQL，直接按 Ctrl + C


## 用 MySQL 客户端访问 Lealone 数据库

执行以下命令启动 MySQL 客户端:

`mysql --no-beep -h localhost -P 9310 -u root`

```sql
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 0
Server version: 5.1.48-lealone-5.0.0

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


## 用 PostgreSQL 客户端访问 Lealone 数据库

执行以下命令启动 PostgreSQL 客户端:

`psql -h 127.0.0.1 -p 9510 -U postgres -W`

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