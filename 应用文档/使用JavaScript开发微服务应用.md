# 使用 JavaScript 在 Lealone 中开发微服务应用

## 运行需要

* JDK 1.8+

* 支持 ECMAScript 2021


## 下载 Lealone-Polyglot

[lealone-polyglot-5.0.0.jar](https://github.com/lealone/Lealone-Docs/releases/download/lealone-docs-1.0.0/lealone-polyglot-5.0.0.jar)

Lealone-Polyglot 只有一个 jar 包，下载下来之后随意放到一个目录即可

也可以[从源代码构建最新版本](https://github.com/lealone/Lealone-Plugins/tree/master/polyglot)


## 启动 Lealone-polyglot

打开一个新的命令行窗口，运行: `java -jar lealone-polyglot-5.0.0.jar`

```java
Lealone version: 5.0.0-SNAPSHOT
Loading config from jar:file:/E:/lealone-polyglot-5.0.0/lealone-polyglot-5.0.0.jar!/lealone.yaml
Base dir: .\lealone_data
Init storage engines: 7 ms
Init transaction engines: 76 ms
Init sql engines: 5 ms
Init protocol server engines: 124 ms
Init lealone database: 82 ms
Starting tcp server accepter
TcpServer started, host: 127.0.0.1, port: 9210
Web root: ./web
Sockjs path: /_lealone_sockjs_/*
HttpServer is now listening on port: 9000
HttpServer started, host: 127.0.0.1, port: 9000
Total time: 681 ms (Load config: 97 ms, Init: 299 ms, Start: 285 ms)
Exit with Ctrl+C
```

要停止 Lealone-polyglot，直接按 Ctrl + C


## 使用 JavaScript 开发微服务应用

用 JavaScript 编写两个普通函数

./js/my_js_service.js

```JavaScript
function hello(name) {
    return "hello " + name;
}

function getCurrentTime() {
    return new Date();
}
```

## 在 Lealone 数据库中创建服务

打开一个新的命令行窗口，运行: `java -jar lealone-polyglot-5.0.0.jar -client`

执行以下 SQL 创建 my_js_service

```sql
create service if not exists my_js_service (
  hello(name varchar) varchar,
  get_current_time() varchar
)
language 'js' implement by './js/my_js_service.js';
```

执行以下 SQL 就可以直接调用 my_js_service 了

```sql
sql> execute service my_js_service hello('test');
+-------------------------+
| 'MY_JS_SERVICE.HELLO()' |
+-------------------------+
| hello test              |
+-------------------------+
(1 row, 2 ms)

sql> execute service my_js_service get_current_time();
+------------------------------------+
| 'MY_JS_SERVICE.GET_CURRENT_TIME()' |
+------------------------------------+
| 2022-04-21T07:19:33.441Z           |
+------------------------------------+
(1 row, 2 ms)

sql>
```

## 在浏览器中调用后端服务

直接打开以下 URL 即可

http://localhost:9000/service/my_js_service/hello?name=zhh

http://localhost:9000/service/my_js_service/get_current_time
