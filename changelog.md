# Change Log

# [Lealone 5.2.1](https://github.com/lealone/Lealone/releases/tag/lealone-5.2.1) (2023-12-15)

## bug fix

#### database

* [Issue #207](https://github.com/lealone/Lealone/issues/207#issuecomment-1782552948) 调度服务线程拿到表名列表后，如果接下来在进行fullGc前表被其他线程删除了，会导致NPE

* [Issue #207](https://github.com/lealone/Lealone/issues/207) 在一个大事务中写入数据频繁产生 page split 后有可能产生堆栈溢出错误，标记脏页时产生 Position 0 错误 

* 如果一张表存在 lob 字段,当删除这张表时会错误关闭其他表对应的 BTreeMap 从而抛出 IllegalStateException: This map is closed

* 修改表结构后，版本号不为0的老记录执行 redo 时会多执行一遍 ddl 语句


## improvement

#### database

* DEFAULT_MAX_MEMORY_ROWS 从1万改成10万，避免查询的数据量很小时也写临时文件



# [Lealone 5.2.0](https://github.com/lealone/Lealone/releases/tag/lealone-5.2.0) (2023-09-15)

## bug fix

#### database

* 如果执行 insert 语句时执行 prepared 类型的 select 语句，
  当 select 语句让出执行权时可能会导致 insert 语句被执行两次

* 对于无主键的表如果存在索引，在多个线程写索引时可能导致索引 key 指向的 row id 是 0

* 如果保存数据的过程中有新的事务进来，不能增加 checkpoint，否则断电时会导致数据丢失

* 在手动提交模式下，当执行语句失败时如果没有手工回滚就直接执行下一条语句会被挂起

* 当新事务提交更新后，在旧的 REPEATABLE_READ 事务下执行更新语句会导致更新丢失问题

* 在自动提交模式下，嵌套执行的 sql 语句不应该提前提交事务

* 调度器在一个事务中先执行一条 sql，然后再执行 select，如果给其他事务让出执行权后 select 会被挂起

* [Issue #177](https://github.com/lealone/Lealone/issues/177) 在聚合函数中使用 case 表达式会出现类型错误

* [Issue #180](https://github.com/lealone/Lealone/issues/180) 在 shell 脚本中$@需要加上双引号才能把命令行中带双引号的字符串当成一个整体处理

* [Issue #190](https://github.com/lealone/Lealone/issues/190) 保存 RemovedPages 时使用一个拷贝，避免产生 ConcurrentModificationException 

* [Issue #196](https://github.com/lealone/Lealone/issues/196) 存在多个事务时，执行不带 where 条件的 count(*) 可能产生 NPE


## improvement

#### database

* 执行 shutdown server 语句需要 lealone database 的 admin 权限

* 初始化数据库时把表的版本号也初始化了，避免在执行 insert/update 时用同步的方式加载表的版本号

* 只允许一个线程初始化数据库，其他线程异步等待，但是不会挂起

* 使用 ConcurrentHashMap 实现 UniqueHashIndex，支持多线程并发读写

* 每条记录在内存中不再需要包装成一个 ValueArray 对象，能节省5个字节的内存大小

* 二级索引的 Key 由 ValueArray 类型变成 IndexKey 类型，能节省5个字节的内存大小

* 不再需要 btree page 缓存，会自动根据内存使用情况动态回收 btree page 占用的内存空间

* 修改记录时，如果存在可重复读的事务并且记录的历史版本足够这些事务使用了，那就不必再把记录的旧值加到历史版本链表里了

* 执行 checkpoint 分两步进行，每一步只需要放一条 checkpoint log 到 log sync 线程的队列即可，无需等待

* 优化 sequence 的实现，并且事务回滚时可以退回到之前的值

* 事务提交时，无需把被更新的记录 put 回 btree map 中，标记一下脏页即可

* 只要 chunk 文件的大小没有超过 chunk_max_size，脏页数据会一直 append 到最新的 chunk 文件

* 优化IN(SELECT)语句，避免每次都查询 [Issue #189](https://github.com/lealone/Lealone/issues/189)

* 数据库重新启动或网络断开后 lealone 命令行客户端会自动重连

* 创建索引的过程中不再需要创建多个临时 btree map


#### lealone-plugins

* xsql、storage、bench 模块已经转到 qinsql 项目



## new features

#### database

* 执行 checkpoint 时会把老的 redo log 文件归档

* 支持在 lealone.yaml 配置文件中使用环境变量

* 单个表可以拥有自己的专属 lob 存储

* 支持 shutdown server 语句

* 支持多个 net client，能大幅提升性能

* jdbc client 支持阻塞 IO，能提升执行 jdbc 同步 api 的性能

* 表数据损坏时可以通过 repair table 语句修复



# [Lealone 5.1.2](https://github.com/lealone/Lealone/releases/tag/lealone-5.1.2) (2023-03-14)

## bug fix

#### database

* 多个事务同时使用 SET 语句修数据库参数时未加锁会导致并发问题

* 对无主键的表进行append操作时可能产生错乱的row id



## improvement

#### database

* 每条记录在内存中不再需要 commitTimestamp 字段，能节省8个字节的内存大小

* 优化行锁的性能，无需在一个 ConcurrentHashMap 中增加删除查询 LockOwner

* 执行 select for update 时不再需要在内存中生成 undo log，直接加行锁即可


## new features

#### database

* 可以配置客户端 SocketChannel 参数(socket_recv_buffer_size/socket_send_buffer_size)



# [Lealone 5.1.1](https://github.com/lealone/Lealone/releases/tag/lealone-5.1.1) (2023-01-15)

## bug fix

#### database

* 使用 SystemSession 创建嵌入式数据库接着马上访问它会找不到数据库

* 事务因为锁超时后未正确设置 Session 状态导致无法执行某些 SQL 语句(比如 SET 语句)

* 执行 DDL 语句遇到对象锁时重复设置 Session 状态为 WAITING 有可能导致当前事务会被挂起

#### lealone-plugins-test

* [Issue #23](https://github.com/lealone/Lealone-Plugins/issues/23): lealone-plugins-test 模块找不到 lealone-test 模块的类


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

