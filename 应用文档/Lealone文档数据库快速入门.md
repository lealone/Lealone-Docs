# 1. Lealone 文档数据库快速入门

Lealone 是一个高度兼容 MongoDB 的文档数据库，可以使用 MongoDB 的各种客户端访问 Lealone。


### 1.1. 运行需要

* [JDK 1.8+](https://www.oracle.com/java/technologies/downloads/)


### 1.2. 下载 Lealone

[lealone-6.0.0.jar](https://github.com/lealone/Lealone/releases/download/lealone-6.0.0/lealone-6.0.0.jar)

Lealone 只有一个 jar 包，下载下来之后随意放到一个目录即可

也可以从源代码构建最新版本，请阅读文档: [从源码构建 Lealone](https://github.com/lealone/Lealone-Docs/blob/master/%E5%BA%94%E7%94%A8%E6%96%87%E6%A1%A3/%E4%BB%8E%E6%BA%90%E7%A0%81%E6%9E%84%E5%BB%BALealone.md)


### 1.3. 启动 Lealone 数据库

打开一个新的命令行窗口，运行: `java -jar lealone-6.0.0.jar`

```java
Lealone version: 6.0.0
Use default config
Base dir: ./lealone_data
Init storage engines: 5 ms
Init transaction engines: 46 ms
Init sql engines: 4 ms
Init protocol server engines: 13 ms
Init lealone database: 119 ms
TcpServer started, host: 127.0.0.1, port: 9210
Total time: 207 ms (Load config: 2 ms, Init: 201 ms, Start: 4 ms)
Exit with Ctrl+C
```

要停止 Lealone，直接按 Ctrl + C


### 1.4. 使用 MongoDB Shell 客户端执行命令访问 Lealone

需要下载 [MongoDB Shell 客户端](https://www.mongodb.com/try/download/shell)

打开一个新的命令行窗口，运行: `mongosh mongodb://127.0.0.1:27017/test`

```sql
Connecting to:          mongodb://127.0.0.1:27017/test?directConnection=true&serverSelectionTimeoutMS=2000&appName=mongosh+1.9.1
Using MongoDB:          6.0.0
Using Mongosh:          1.9.1

For mongosh info see: https://docs.mongodb.com/mongodb-shell/

test> db.runCommand({ insert: "c1", documents: [{ _id: 1, user: "u1", age: 12, status: "A"}] });
{ ok: 1, n: 1 }
test>

test> db.runCommand({ find: "c1", filter: {_id: 1} });
{
  cursor: {
    id: Long("0"),
    ns: 'test.c1',
    firstBatch: [ { _id: Long("1"), user: 'u1', age: 12, status: 'A' } ]
  },
  ok: 1
}
test>

test> db.runCommand({ update: "c1", updates: [ { q: { _id: 1 }, u: { $set: { user: "u11" } } }] });
{ ok: 1, n: 1, nModified: 1 }
test>

test> db.runCommand({"aggregate": "c1", "pipeline": [{"$match": {}}, {"$group": {"_id": 1, "n": {"$sum": 1}}}], "cursor": {}});
{
  cursor: { id: Long("0"), ns: 'test.c1', firstBatch: [ { _id: 1, n: 1 } ] },
  ok: 1
}
test>

test> db.runCommand({ delete: "c1", deletes: [ { q: { _id: 1 }, limit: 1 } ] });
{ ok: 1, n: 1 }
test>

test> db.runCommand({ find: "c1", filter: {_id: 1} });
{ cursor: { id: Long("0"), ns: 'test.c1', firstBatch: [] }, ok: 1 }
test>
```


# 2. MongoDB 文档

MongoDB 各种命令的用法请参考 [官方文档](https://www.mongodb.com/docs/manual/crud/)
