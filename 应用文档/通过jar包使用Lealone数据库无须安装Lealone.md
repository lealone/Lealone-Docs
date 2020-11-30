如果一个数据库，不管是在单机场景还是分布式集群场景，都不需要事先安装，只需要通过jar包依赖的方式被应用按需使用，然后应用自己负责部署，这种模式听起来是不是很有趣，来跟我一起体验一下 Lealone 的神奇玩法。​​​​

文章最后有完整的项目代码，可直接下载到本机上尝试。

### 1. 创建一个 maven 项目，在 pom.xml 文件中增加对 Lealone 的依赖

```xml
    <dependencies>
        <dependency>
            <groupId>org.lealone</groupId>
            <artifactId>lealone-main</artifactId>
            <version>5.0.0-SNAPSHOT</version>
        </dependency>
    </dependencies>

    <repositories>
        <repository>
            <id>github</id>
            <url>https://maven.pkg.github.com/lealone/Lealone</url>
        </repository>
    </repositories>
```

然后在 pom.xml 文件使用 maven-shade-plugin 把应用和 Lealone 的代码打成一个 jar 包

```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>2.3</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <transformers>
                                <!-- 合并多个服务接口文件，就是在META-INF/services目录下的那些文件，可能有多个 -->
                                <transformer
                                    implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer" />
                                    
                                <transformer
                                    implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <manifestEntries>
                                        <Main-Class>org.lealone.examples.runmode.RunModeDemo</Main-Class>
                                    </manifestEntries>
                                </transformer>
                            </transformers>
                            <outputFile>target/lealone-runmode-demo-all-${project.version}.jar</outputFile>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

这里特意加了一个 Main-Class，后面我们会创建一个启动类: org.lealone.examples.runmode.RunModeDemo

加了 Main-Class 后，可以直接像这样运行我们的应用: java -jar lealone-runmode-demo-all-1.0.0.jar



### 2. 创建启动类

在 src/main/java 目录下创建启动类: org.lealone.examples.runmode.RunModeDemo

```java
package org.lealone.examples.runmode;

import org.lealone.main.Lealone;

public class RunModeDemo {
    public static void main(String[] args) {
        Lealone.main(args);
    }
}
```

这个启动类的逻辑很简单，直接调用 Lealone 的启动类即可


### 3. 创建启动配置文件

Lealone 可以不用配置文件，全通过程序化的方式启动，不过通过配置文件的方式使用更简单

在 src/main/resources 目录下创建配置文件: lealone.yaml

大多数情况下都可以直接复制以下内容，修改少许内容即可:

```yaml
base_dir: ./target/data

listen_address: 127.0.0.1

storage_engines:
  - name: AOSE
    enabled: true
    parameters: {
        # key1: v1,
        # key2: v2,
    }

transaction_engines:
  - name: AOTE
    enabled: true
    parameters: {
        redo_log_dir: redo_log
    }

sql_engines:
  - name: Lealone
    enabled: true
    parameters: {
        # key1: v1,
        # key2: v2,
    }

protocol_server_engines:
  - name: TCP
    enabled: true
    parameters: {
        port: 9210,
        trace: false,
        allow_others: true,
        daemon: false,
        ssl: false
    }
  - name: HTTP
    enabled: true
    parameters: {
        port: 8080,
        web_root: ./web,
        jdbc_url: "jdbc:lealone:embed:test"
    }
  - name: P2P
    enabled: true
    parameters: {
        port: 9211,
        ssl: false
    }

server_encryption_options:
    keystore: conf/.keystore
    keystore_password: lealone
    truststore: conf/.truststore
    truststore_password: lealone

cluster_config:
    cluster_name: Test Cluster
    node_snitch: SimpleSnitch
    dynamic_snitch: true
    request_timeout_in_ms: 10000

    seed_provider:
        name: SimpleSeedProvider
        parameters: {
            seeds: "127.0.0.1"
        }

    replication_strategy:
        name: SimpleStrategy
        parameters: {
            replication_factor: 3
        }
```


### 4. 在 Eclipse 或其他 IDE 中启动应用

到这里就可以提前运行一下启动类: org.lealone.examples.runmode.RunModeDemo

如果启动成功会看到如下的结果:

```java
INFO 22:11:28.722 Lealone version: 5.0.0-SNAPSHOT
INFO 22:11:28.733 Loading config from file:/E:/lealone/lealone-examples/runmode-demo/target/classes/lealone.yaml
INFO 22:11:28.944 Base dir: ./target/data
INFO 22:11:29.009 Init storage engines: 6 ms
INFO 22:11:29.091 Init transaction engines: 81 ms
INFO 22:11:29.104 Init sql engines: 12 ms
INFO 22:11:29.148 Init protocol server engines: 43 ms
INFO 22:11:29.331 Init lealone database: 183 ms
INFO 22:11:29.357 Starting nio net server
INFO 22:11:29.877 TcpServer started, host: 127.0.0.1, port: 9210
INFO 22:11:31.458 HttpServer started, host: 127.0.0.1, port: 8080
INFO 22:11:31.458 Loading persisted node info
INFO 22:11:31.473 web root: ./web
INFO 22:11:31.474 sockjs path: /_lealone_sockjs_/*
INFO 22:11:31.474 http server is now listening on port: 8080
INFO 22:11:31.556 Starting up server gossip
INFO 22:11:31.566 Starting up LoadBroadcaster
INFO 22:11:31.568 Node [host=127.0.0.1, port=9211] state jump to normal
INFO 22:11:31.568 Starting nio net server
INFO 22:11:31.570 P2pServer started, host: 127.0.0.1, port: 9211
INFO 22:11:31.571 Total time: 2845 ms (Load config: 218 ms, Init: 413 ms, Start: 2214 ms)
INFO 22:11:31.571 Exit with Ctrl+C
```

### 5. 建表、创建后端服务

在 src/main/resources 目录下创建文件: tables.sql，可以把建表语句统一放入这个文件中:

```sql
-- 创建表: user，会生成一个名为User的模型类
create table if not exists user (
  id long auto_increment primary key,
  name varchar,
  age int
)
package 'org.lealone.examples.fullstack.generated.model' -- User类所在的包名
generate code './src/main/java' -- User类的源文件所在的根目录
``` 
这一步用于创建user表，加了新的扩展语法，
如果指定generate code，会生成一个名为User的模型类。


在 src/main/resources 目录下创建文件: services.sql，可以把创建后端服务的语句统一放入这个文件中:

```sql
-- 创建服务: user_service，会生成一个对应的UserService接口
create service if not exists user_service (
  add_user(name varchar, age int) long, -- 定义UserService接口方法 add_user
  find_by_name(name varchar) user -- 定义UserService接口方法 find_by_name
)
package 'org.lealone.examples.runmode.generated.service' -- UserService接口所在的包名
implement by 'org.lealone.examples.runmode.UserServiceImpl' -- UserService接口的默认实现类
generate code './src/main/java' -- UserService接口源文件的根目录
```

这一步用于描述一个服务的相关信息，比如它有哪些可调用的方法，
会为每个服务生成一个对应的Java接口，并指定服务接口的默认实现类。
通过create service语句创建的服务在内部会被托管，
服务的注册与发现功能已经内置在框架当中，不需要再依赖其他第三方组件。


可以写一个工具类用来执行上面的 SQL 脚本:

```java
package org.lealone.examples.runmode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class RunSqlScript {

    public static void main(String[] args) throws Exception {
        String jdbcUrl = "jdbc:lealone:tcp://localhost/lealone?user=root&password=";
        runSql(jdbcUrl, "create database if not exists test");

        // 执行建表脚本，同时自动生成对应的模型类的代码
        runScript("./src/main/resources/tables.sql");

        // 执行服务创建脚本，同时自动生成对应的服务接口代码
        runScript("./src/main/resources/services.sql");
    }

    static void runScript(String scriptFile) throws Exception {
        String jdbcUrl = "jdbc:lealone:tcp://localhost/test?user=root&password=";
        runSql(jdbcUrl, "RUNSCRIPT FROM '" + scriptFile + "'");
    }

    static void runSql(String url, String sql) throws Exception {
        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("execute sql: " + sql);
        }
    }
}
```

### 6. 实现后端服务

```java
package org.lealone.examples.fullstack;

import org.lealone.examples.fullstack.generated.model.User;
import org.lealone.examples.fullstack.generated.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public Long addUser(String name, Integer age) {
        // 如果 name = 'zhh', age = 18
        // 对应的sql是: insert into user(name, age) values('zhh', 18);
        return new User().name.set(name).age.set(age).insert(); // 链式调用，insert()返回新增记录的rowId
    }

    @Override
    public User findByName(String name) {
        // 如果 name = 'zhh'
        // 对应的sql是: select * from user where name = 'zhh' limit 1
        return User.dao.where().name.eq(name).findOne();
    }
}
```

服务实现类就是一个最普通的接口实现类，服务框架对服务实现类是无侵入的。

这里也演示了Lealone ORM框架的基本用法，ORM框架对应用代码也是无侵入的。

Lealone ORM框架的更多用法演示可以尝试一下这个
[lealone-orm-demo](https://github.com/lealone/Lealone-Examples/tree/main/orm-demo)

[Lealone ORM 框架快速入门](https://github.com/lealone/Lealone-Examples/blob/main/docs/Lealone%20ORM%E6%A1%86%E6%9E%B6%E5%BF%AB%E9%80%9F%E5%85%A5%E9%97%A8.md)


### 7. 调用后端服务

可以通过三种方式调用后端服务: RESTfull URL、后端 RPC Client、前端 RPC Client

#### 7.1. 通过 RESTfull URL 调用后端服务

URL 的格式是: /service/:serviceName/:methodName?paramName1=paramValue1&paramName2=paramValue2

例如:

add_user: http://localhost:8080/service/user_service/add_user?name=zhh&age=18

find_by_name: http://localhost:8080/service/user_service/find_by_name?name=zhh


#### 7.2. 通过后端 RPC Client 调用后端服务

```java
package org.lealone.examples.runmode;

import org.lealone.examples.runmode.generated.model.User;
import org.lealone.examples.runmode.generated.service.UserService;

public class UserServiceConsumer {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:lealone:tcp://localhost/test?user=root&password=";
        UserService s = UserService.create(jdbcUrl);

        String name = "zhh";

        Long rowId = s.addUser(name, 18);

        System.out.println("add user: " + name + ", return id: " + rowId);

        User user = s.findByName(name);

        System.out.println("user: " + user);
    }
}
```

#### 7.3. 通过前端 RPC Client 调用后端服务

```html
<!doctype html>
<html>

<head>
    <script src="sockjs-1.1.2.js"></script>
    <script src="lealone-5.0.0.js"></script>
    <script src="vue.min-2.3.3.js"></script>
</head>

<body lang="en">
    <h1>run mode test</h1>

    <div id="add">
        name: <input v-model="name">
        age <input v-model="age">
        <button v-on:click="addUser">add</button>
        <p>{{ message }}</p>
    </div>

    <div id="find"> 
        name: <input v-model="name">
        <button v-on:click="findByName">find</button>
        <p>{{ message }}</p>
    </div>

    <script>
    //获得一个服务代理
    var userService = lealone.getService("user_service");

    new Vue({
        el: '#add',
        data: {
            name: "zhh",
            age: "18",
            message: ""
        },
        methods: {
            addUser: function() {
                var that = this;
                //调用服务
                userService.addUser(this.name, this.age, function(id) {
                    that.message = "add user: " + that.name + ", return id: " + id;
                });
            }
        }
    });
    new Vue({
        el: '#find',
        data: {
            name: "zhh",
            message: ""
        },
        methods: {
            findByName: function() {
                var that = this;
                //调用服务
                userService.findByName(this.name, function(user) {
                    that.message = "user: " + user;
                });
            }
        }
    })
    </script>
</body>
</html>
```

lealone-5.0.0.js 相当于一个RPC框架的客户端，通过sockjs与后端RPC框架通信，
通过lealone.getService()方法获得一个服务代理后就可以直接调用后端服务了。




### 8. 完整例子

下载项目 [lealone-runmode-demo](https://github.com/lealone/Lealone-Examples/tree/main/runmode-demo)


### 9. 打包构建

执行: build -p

非 Windows 系统执行: mvn package -Dmaven.test.skip=true



### 10. 运行单机版

执行: build -r

非 Windows 系统执行: java -jar ./target/lealone-runmode-demo-all-1.0.0.jar


如果之前在 Eclipse 中的进程还在运行，需要先关掉


启动成功后，先执行 SQL 脚本:

java -cp ./target/lealone-runmode-demo-all-1.0.0.jar org.lealone.examples.runmode.RunSqlScript


然后在浏览器中打开下面这个 URL 进行测试:
http://localhost:8080/runmode.html

或者用下面的 URL 测试:

add_user: http://localhost:8080/service/user_service/add_user?name=zhh&age=18

find_by_name: http://localhost:8080/service/user_service/find_by_name?name=zhh


还可以执行后端 RPC Client 进行测试:

java -cp ./target/lealone-runmode-demo-all-1.0.0.jar org.lealone.examples.runmode.UserServiceConsumer


### 11. 运行分布式集群版

新建 node2、node3 两个目录，然后把 src/main/resources/lealone.yaml 文件各拷贝一份到 node2、node3 目录中

接着修改 node2/lealone.yaml 文件，把 listen_address 改成 127.0.0.2

类似的，修改 node3/lealone.yaml 文件，把 listen_address 改成 127.0.0.3

最后另外打开两个命令行窗口，切换到 node2、node3 目录，然后分别执行以下命令即可:

java -jar ../target/lealone-runmode-demo-all-1.0.0.jar -Dlealone.config=./lealone.yaml

通过这种简单的方式就可以跟第 10 步启动的 Lealone 构成一个 3 节点的 Lealone 集群。


到这里如果把上面的测试 URL 改成 127.0.0.2:8080 或 127.0.0.3:8080 会发现找不到服务，这是正常的，</br>
因为服务还只是部署在 127.0.0.1 节点上，后续文章会介绍如何把服务自动扩容到多个节点上。


### 12. 运行 Lealone SQL Shell

Lealone 还提供了一个 SQL Shell，可以用 SQL Shell 执行所有的 SQL 语句，打开一个命令行窗口执行以下命令即可:

java -cp ./target/lealone-runmode-demo-all-1.0.0.jar org.lealone.main.Shell -url jdbc:lealone:tcp://localhost:9210/lealone -user root

### 13. 可能出现的问题

如果执行 build -p 找不到 Lealone 的依赖包，
需要下载以下项目的代码: 

[lealone-database](https://github.com/lealone/Lealone)


执行 build -i 把它们安装到本地的maven仓库即可。

