# 1. Lealone 数据库快速入门


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


### 1.4. 使用 Lealone 客户端执行 SQL 语句

打开一个新的命令行窗口，运行: `java -jar lealone-6.0.0.jar -client`

默认用 `root` 用户连到 127.0.0.1:9210，密码为空

默认数据库是 `lealone`，可以通过 `-database` 参数指定其他数据库

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

sql> CREATE TABLE IF NOT EXISTS test (f1 int primary key, f2 long);
(Update count: 0, 7 ms)

sql> INSERT INTO test(f1, f2) VALUES(1, 2);
(Update count: 1, 2 ms)

sql> SELECT * FROM test;
+----+----+
| F1 | F2 |
+----+----+
| 1  | 2  |
+----+----+
(1 row, 8 ms)

sql> UPDATE test SET f2 = 20;
(Update count: 1, 1 ms)

sql> SELECT * FROM test;
+----+----+
| F1 | F2 |
+----+----+
| 1  | 20 |
+----+----+
(1 row, 2 ms)

sql> DELETE FROM test;
(Update count: 1, 2 ms)

sql> SELECT * FROM test;
+----+----+
| F1 | F2 |
+----+----+
+----+----+
(0 rows, 1 ms)
```


### 1.5. CRUD Example

只需要懂 JDBC 和 SQL 就可以轻松看懂下面的代码:

```java
import java.sql.*;

public class CRUDExample {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:lealone:tcp://localhost:9210/lealone";
        Connection conn = DriverManager.getConnection(url, "root", "");
        Statement stmt = conn.createStatement();

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS test (f1 int primary key, f2 long)");
        stmt.executeUpdate("INSERT INTO test(f1, f2) VALUES(1, 1)");
        stmt.executeUpdate("UPDATE test SET f2 = 2 WHERE f1 = 1");
        ResultSet rs = stmt.executeQuery("SELECT * FROM test");
        while (rs.next()) {
            System.out.println("f1=" + rs.getInt(1) + " f2=" + rs.getLong(2));
            System.out.println();
        }
        rs.close();
        stmt.executeUpdate("DELETE FROM test WHERE f1 = 1");
        stmt.executeUpdate("DROP TABLE IF EXISTS test");
        stmt.close();
        conn.close();
    }
}
```
把上面的代码存到一个 CRUDExample.java 文件 (目录随意) <br>

编译: 
> javac CRUDExample.java

运行: 
> java -cp .;lealone-6.0.0.jar CRUDExample


### 1.6. Lealone Client Maven 依赖

```xml
<dependencies>
    <dependency>
        <groupId>com.lealone</groupId>
        <artifactId>lealone-client</artifactId>
        <version>6.0.0</version>
    </dependency>
</dependencies>
```


# 2. UI 客户端

可以使用 [DBeaver](https://dbeaver.io/) 这类 JDBC UI 客户端访问 Lealone

[DBeaver 配置文档](https://github.com/lealone/Lealone-Docs/blob/master/应用文档/使用DBeaver访问Lealone数据库.md)



# 3. SQL Reference

因为 Lealone 的 SQL 引擎从 [H2 数据库](http://www.h2database.com/html/main.html) 的 SQL 引擎发展而来， 
所以 Lealone 的 SQL 用法与 H2 数据库一样。


* [SQL Grammar](http://118.178.254.54/grammar.html)

* [Functions](http://118.178.254.54/functions.html)

* [Data Types](http://118.178.254.54/datatypes.html)
