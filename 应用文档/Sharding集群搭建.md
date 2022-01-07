## Lealone 既能在一台服务器上搭建 Sharding 集群，也可以在多台服务器上搭建。


## 1. 在一台服务器上搭建 Lealone Sharding 集群


### 1.1. 运行需要

* JDK 1.8+


### 1.2. 下载 Lealone

[lealone-5.0.0.tar.gz](https://github.com/lealone/Lealone-Docs/releases/download/lealone-docs-1.0.0/lealone-5.0.0.tar.gz)
或
[lealone-5.0.0.zip](https://github.com/lealone/Lealone-Docs/releases/download/lealone-docs-1.0.0/lealone-5.0.0.zip)

压缩文件下载下来之后直接解压到任意目录即可，

下文假设解压到 `D:\lealone-5.0.0` 目录(非 windows 操作系统请把 D:\lealone-5.0.0 替换成实际的目录名)

也可以从源代码构建最新版本，请阅读文档: [从源码构建 Lealone](https://github.com/lealone/Lealone-Docs/blob/master/%E5%BA%94%E7%94%A8%E6%96%87%E6%A1%A3/%E4%BB%8E%E6%BA%90%E7%A0%81%E6%9E%84%E5%BB%BALealone.md)


### 1.3. 启动 Lealone 集群

打开一个新的命令行窗口，进入 `D:\lealone-5.0.0\bin` 目录，

如果是 windows 操作系统，可以在命令行窗口中执行以下命令启动一个带有 3 个节点的集群:

`lealone -nodes 3`

非 windows 操作系统，可以打开三个新的命令行窗口，都切换到 D:\lealone-5.0.0\bin 目录，然后在每个窗口中分别运行如下三条命令:

`java -Dlealone.logdir=../logs/cluster/node1 -cp ../conf:../lib/* org.lealone.main.Lealone -cluster 1`

`java -Dlealone.logdir=../logs/cluster/node2 -cp ../conf:../lib/* org.lealone.main.Lealone -cluster 2`

`java -Dlealone.logdir=../logs/cluster/node3 -cp ../conf:../lib/* org.lealone.main.Lealone -cluster 3`


### 1.4. 创建使用 Sharding 模式运行的数据库

打开一个新的命令行窗口，进入 `D:\lealone-5.0.0\bin` 目录，

运行: `sqlshell -url jdbc:lealone:tcp://localhost:9210/lealone -user root`

```java
D:\lealone-5.0.0\bin>sqlshell -url jdbc:lealone:tcp://localhost:9210/lealone -user root

Welcome to Lealone Shell 5.0.0
Exit with Ctrl+C
Commands are case insensitive; SQL statements end with ';'
help or ?      Display this help
list           Toggle result list / stack trace mode
maxwidth       Set maximum column width (default is 100)
autocommit     Enable or disable autocommit
history        Show the last 20 statements
quit or exit   Close the connection and exit

//创建 mydb_sharding 数据库，每条记录复制一份，记录会分散在两个节点上
sql> CREATE DATABASE mydb_sharding RUN MODE sharding PARAMETERS(replication_factor: 1, assignment_factor: 2);
(Update count: 0, 78 ms)

//查看 mydb_sharding 数据库的数据放在哪些节点上
sql> select * from information_schema.databases;
+------------------+---------------+-------------------------------+
| DATABASE_NAME    | RUN_MODE      | NODES                         |
+------------------+---------------+-------------------------------+
| MYDB_REPLICATION | REPLICATION   | 127.0.0.2:9210,127.0.0.1:9210 |
| MYDB_SHARDING    | SHARDING      | 127.0.0.3:9210,127.0.0.2:9210 |
| lealone          | CLIENT_SERVER | null                          |
+------------------+---------------+-------------------------------+
(3 rows, 32 ms)

```

### 1.5. 访问使用 Sharding 模式运行的数据库

打开一个新的命令行窗口，进入 `D:\lealone-5.0.0\bin` 目录，

运行: `sqlshell -url jdbc:lealone:tcp://localhost:9210/mydb_sharding -user root`

```java
D:\lealone-5.0.0\bin>sqlshell -url jdbc:lealone:tcp://localhost:9210/mydb_sharding -user root

Welcome to Lealone Shell 5.0.0
Exit with Ctrl+C
Commands are case insensitive; SQL statements end with ';'
help or ?      Display this help
list           Toggle result list / stack trace mode
maxwidth       Set maximum column width (default is 100)
autocommit     Enable or disable autocommit
history        Show the last 20 statements
quit or exit   Close the connection and exit

sql> create table test(f1 int primary key, f2 int);
(Update count: 0, 63 ms)

sql> insert into test values(1,2);
(Update count: 1, 16 ms)

sql> select * from test;
+----+----+
| F1 | F2 |
+----+----+
| 1  | 2  |
+----+----+
(1 row, 3 ms)

sql>
```


## 2. 在多台服务器上搭建 Lealone Sharding 集群

TODO

