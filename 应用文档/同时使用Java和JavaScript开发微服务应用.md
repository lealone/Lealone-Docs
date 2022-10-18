用 Lealone 开发微服务应用时，可以混合使用多种语言进行开发。

这篇文章介绍同时使用 Java 和 JavaScript 开发微服务应用。

文章最后有完整的项目代码，可直接下载到本机上尝试。


### 在 pom.xml 中增加依赖

```xml
    <dependencies>
        <dependency>
            <groupId>org.lealone.plugins</groupId>
            <artifactId>lealone-service</artifactId>
            <version>5.0.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>org.lealone.plugins</groupId>
            <artifactId>lealone-vertx</artifactId>
            <version>5.0.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>org.lealone.plugins</groupId>
            <artifactId>lealone-javascript</artifactId>
            <version>5.0.0-SNAPSHOT</version>
        </dependency>
    </dependencies>

    <repositories>
        <repository>
            <id>ossrh</id>
            <url>https://oss.sonatype.org/content/repositories/snapshots</url>
        </repository>
    </repositories>
```

其中的 lealone-service 表示引入 Lealone 微服务框架，lealone-vertx 是把 vertx 当成 http server，而 lealone-javascript 是对 JavaScript 的支持。


### 用 Java 和 JavaScript 实现服务

这里用 JavaScript 实现一个 hello 服务，假设保存到 ./js/hello_service.js

```JavaScript
    function hello(name) {
	return "hello " + name;
    }
```

然后用 Java 实现一个返回当前时间的服务

```java
package org.lealone.examples.polyglot;

public class TimeService {
    public String getCurrentTime() {
        return new java.util.Date().toString();
    }
}
```

以上用 Java 和 JavaScript 实现的服务代码都是最普通的代码，无需引入 Lealone 的任何东西。



### 用 SQL 创建服务，与 Java 和 JavaScript 实现的服务绑定

```SQL
-- 创建 hello_service 服务，用 js 实现
create service if not exists hello_service (
  hello(name varchar) varchar
)
language 'js' implement by './js/hello_service.js';
```

```SQL
-- 创建 time_service 服务，默认用 java 语言实现
create service if not exists time_service (
  get_current_time() varchar
)
implement by 'org.lealone.examples.polyglot.TimeService';
```

SQL 脚本可以存放到一个 services.sql 文件中方便运行。

到这里，一个多语言微服务应用就已经完成了，剩下就是启动应用了。


### 启动 http server

```java
    public static void main(String[] args) throws Exception { 
        HttpServer server = new HttpServer();
        server.start();
    }
```

在浏览器中打开下面的 URL 进行测试:

http://localhost:8080/service/hello_service/hello?name=zhh

http://localhost:8080/service/time_service/get_current_time


### 完整例子

下载项目 [polyglot-demo](https://github.com/lealone/Lealone-Examples/tree/main/polyglot-demo)

源码编译与打包请执行 build -p (或者 mvn package -Dmaven.test.skip=true)

运行例子请执行 build -r (或者 java -jar ./target/lealone-polyglot-demo-all-1.0.0.jar)

如果在 Eclipse 或其它 IDE 中，可以直接运行 PolyglotDemo

```java
package org.lealone.examples.polyglot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.lealone.plugins.service.http.HttpServer;

public class PolyglotDemo {

    // 通过 JDBC 访问的数据库的 URL
    static String jdbcUrl = "jdbc:lealone:embed:test";

    public static void main(String[] args) throws Exception {
        // 启动 HttpServer，请在浏览器中打开下面的 URL 进行测试:
        // http://localhost:8080/service/hello_service/hello?name=zhh
        // http://localhost:8080/service/time_service/get_current_time
        HttpServer server = HttpServer.create();
        server.setJdbcUrl(jdbcUrl);
        server.start();

        createService();
    }

    // 执行 services.sql 脚本，创建服务
    public static void createService() throws Exception {
        try (Connection conn = DriverManager.getConnection(jdbcUrl); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("RUNSCRIPT FROM './src/main/resources/services.sql'");
        }
    }
}
```

### 可能出现的问题

如果执行 build -p 找不到 Lealone 的依赖包，
需要下载以下项目的代码: 

[lealone-database](https://github.com/lealone/Lealone)


执行 build -i 把它们安装到本地的 maven 仓库即可。

