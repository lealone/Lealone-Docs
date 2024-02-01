## Lealone 既能在一台服务器上搭建 Sharding 集群，也可以在多台服务器上搭建。


## 1. 在一台服务器上搭建 Lealone Sharding 集群


### 1.1. 运行需要

* JDK 1.8+


### 1.2. 下载 Lealone

[lealone-6.0.0.jar](https://github.com/lealone/Lealone/releases/download/lealone-6.0.0/lealone-6.0.0.jar)

Lealone 只有一个 jar 包，下载下来之后随意放到一个目录即可

也可以从源代码构建最新版本，请阅读文档: [从源码构建 Lealone](https://github.com/lealone/Lealone-Docs/blob/master/%E5%BA%94%E7%94%A8%E6%96%87%E6%A1%A3/%E4%BB%8E%E6%BA%90%E7%A0%81%E6%9E%84%E5%BB%BALealone.md)


### 1.3. 启动 Lealone 集群

打开3个新的命令行窗口，在每个窗口分别执行以下命令之一，启动一个带有3个节点的集群:

`java -jar lealone-6.0.0.jar -cluster -host 127.0.0.1`

`java -jar lealone-6.0.0.jar -cluster -host 127.0.0.2`

`java -jar lealone-6.0.0.jar -cluster -host 127.0.0.3`


### 1.4. 创建使用 Sharding 模式运行的数据库

打开一个新的命令行窗口，运行: `java -jar lealone-6.0.0.jar -client`

```java
Welcome to Lealone Shell 6.0.0
Connect to jdbc:lealone:tcp://localhost:9210/lealone
Commands are case insensitive; SQL statements end with ';'
help or ?          Display this help
list               Toggle result list / stack trace mode
maxwidth or md     Set maximum column width (default is 100)
autocommit or ac   Enable or disable autocommit
history or h       Show the last 20 statements
reconnect or rc    Reconnect the database
quit or exit       Close the connection and exit

//创建 mydb_sharding 数据库，每条记录复制一份，记录会分散在两个节点上
sql> CREATE DATABASE mydb_sharding RUN MODE sharding PARAMETERS(replication_factor: 1, assignment_factor: 2);
(Update count: 0, 22 ms)

//查看 mydb_sharding 数据库的数据放在哪些节点上
sql> select * from information_schema.databases;
+----+------------------+---------------+-------------------------------+
| ID | DATABASE_NAME    | RUN_MODE      | NODES                         |
+----+------------------+---------------+-------------------------------+
| 5  | MYDB_REPLICATION | REPLICATION   | 127.0.0.2:9210,127.0.0.1:9210 |
| 6  | MYDB_SHARDING    | SHARDING      | 127.0.0.3:9210,127.0.0.1:9210 |
| 0  | lealone          | CLIENT_SERVER | null                          |
+----+------------------+---------------+-------------------------------+
(3 rows, 1 ms)
```


### 1.5. 访问使用 Sharding 模式运行的数据库

打开一个新的命令行窗口，

运行: `java -jar lealone-6.0.0.jar -client -database mydb_sharding`

```java
Welcome to Lealone Shell 6.0.0
Connect to jdbc:lealone:tcp://localhost:9210/mydb_sharding
Commands are case insensitive; SQL statements end with ';'
help or ?          Display this help
list               Toggle result list / stack trace mode
maxwidth or md     Set maximum column width (default is 100)
autocommit or ac   Enable or disable autocommit
history or h       Show the last 20 statements
reconnect or rc    Reconnect the database
quit or exit       Close the connection and exit

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

查看当前目录下的两个子目录

lealone_data\cluster\node_127_0_0_1

lealone_data\cluster\node_127_0_0_3

如果它们都有 db_6 这个目录，就说明数据 sharding 成功了

但是 lealone_data\cluster\node_127_0_0_2 目录下没有 db_6 这个目录，

因为 mydb_sharding 数据库的 sharding 节点不包括 127.0.0.2


## 2. 在多台服务器上搭建 Lealone Sharding 集群

在多台服务器上搭建 Lealone Sharding 集群需要多加一个 -seeds 参数，指定种子节点。

因为 lealone 是对等架构，所以启动阶段需要指定一到多个种子节点，运行时种子节点就没什么用了。

假设有三台服务器，IP 分别是 192.168.0.101、192.168.0.102、192.168.0.103

把 192.168.0.101 当成种子节点，只需要在三台服务器上分别执行以下命令之一即可:

`java -jar lealone-6.0.0.jar -cluster -host 192.168.0.101 -seeds 192.168.0.101`

`java -jar lealone-6.0.0.jar -cluster -host 192.168.0.102 -seeds 192.168.0.101`

`java -jar lealone-6.0.0.jar -cluster -host 192.168.0.103 -seeds 192.168.0.101`

在多台服务器上启动 Lealone Sharding 集群后，创建 mydb_sharding 数据库的操作跟1.4、1.5小节一样。

