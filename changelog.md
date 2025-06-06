# Change Log

# Lealone 6.1.0(未发布)

## bug fix

* [Issue #238](https://github.com/lealone/Lealone/issues/238) 对于枚举字段 ResultSetMetaData.getColumnClassName 返回 java.lang.String

* [Issue #239](https://github.com/lealone/Lealone/issues/239) 嵌入式模式下批量更新会卡住

* 执行 drop database if exists mydb 如果 mydb 不存在会抛错

* JDBC 客户端调用 DataInputStream.read(byte[], int, int) 时有可能读到不完整的数据，导致响应包解析失败

* [Issue #237](https://github.com/lealone/Lealone/issues/237) 使用 database_to_upper=false 参数后重音符号引用的标识符不应该转成大写

* [Issue #253](https://github.com/lealone/Lealone/issues/253) 执行 insert into t select * from t 触发 btree page 切割时会导致无限循环

* 如果在执行 insert/update/delete 时触发 analyzeTable，会修改当前 session 的状态，导致各种并发问题


## new features

* JDBC 客户端支持 LocalDate、LocalTime、LocalDateTime

* JDBC 客户端支持 AUTO_RECONNECT 参数用于自动重连

* 新增 session 参数 disable_generate_code 用于设置是否为 create table 语句生成代码


## improvement

* 优化批量写入，大幅提升性能

* 索引的创建已经异步化了，为大表创建索引无需再等待很久

* 对索引进行 update/delete/insert 时不再需要写 redo log，索引的写操作异步化和并行化了

* 并发更新完记录后释放行锁对象，减少内存占用

* JDBC 客户端也采用统一调度的模式

* 不再使用 buffer pool，所有的 tcp 连接都统一使用调度线程的输入输出 buffer

* 在 chunk 文件头中保存 mapMaxKey 打开表时不用再读取最后一个 page，启动数据库时表很多的话可以提升启动速度



# [Lealone 6.0.1](https://github.com/lealone/Lealone/releases/tag/lealone-6.0.1) (2024-08-18)

## bug fix

* 用0调用 PreparedStatement.setFetchSize，当执行 ResultSet.next 时会导致 IndexOutOfBoundsException

* 通过命令行的方式用嵌入模式运行数据库时无法显示 sql> 提示符

* 执行存储过程时没有对参数进行优化，如果参数不是一个简单的常量值会导致错误


## new features

* 新增 auto_start 参数用于自动启动插件

* 字符串字段支持转义字符


## improvement

* 字符串存储到 BLOB 字段时默认用 UTF_8 编码为字节流



# [Lealone 6.0.0](https://github.com/lealone/Lealone/releases/tag/lealone-6.0.0) (2024-03-20)

## new features

* 使用全新的并发控制系统和线程模型

* 调度器分成了 ClientScheduler、GlobalScheduler、EmbeddedScheduler

* 插件系统更加完善，支持 Create/Drop/Start/Stop Plugin 语句

* 支持文档数据模型，通过插件兼容 MongoDB 的协议和 API

* 支持事务化或非事务化 Sequence

* 支持 Enum 数据类型



# [Lealone 5.2.2](https://github.com/lealone/Lealone/releases/tag/lealone-5.2.2) (2024-03-10)

## bug fix

* [Issue #215](https://github.com/lealone/Lealone/issues/215) 执行 backup 命令时，如果 chunk 文件太大，可用内存不够时会导致 oom

* [Issue #218](https://github.com/lealone/Lealone/issues/218) 用 NULL 更新 lob 字段会导致 NPE



# [Lealone 5.2.1](https://github.com/lealone/Lealone/releases/tag/lealone-5.2.1) (2023-12-15)

## bug fix

* [Issue #207](https://github.com/lealone/Lealone/issues/207#issuecomment-1782552948) 调度服务线程拿到表名列表后，如果接下来在进行fullGc前表被其他线程删除了，会导致NPE

* [Issue #207](https://github.com/lealone/Lealone/issues/207) 在一个大事务中写入数据频繁产生 page split 后有可能产生堆栈溢出错误，标记脏页时产生 Position 0 错误 

* 如果一张表存在 lob 字段,当删除这张表时会错误关闭其他表对应的 BTreeMap 从而抛出 IllegalStateException: This map is closed

* 修改表结构后，版本号不为0的老记录执行 redo 时会多执行一遍 ddl 语句


## improvement

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

