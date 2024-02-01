
这里介绍如何使用 Python 开发一个简单的全栈微服务应用。

基于前端 Vue 框架 + Lealone 微服务框架 + Lealone ORM 框架。

<b>Lealone 的学习成本极低，下文只有启动 Lealone 的代码需要引入 Lealone 的类，</br>
其他应用开发人员负责编写的代码无需导入 Lealone 的任何东西(比如类、接口、注解)。</b>

文章最后有完整的项目代码，可直接下载到本机上尝试。

### 1. 在 pom.xml 中增加依赖

```xml
    <dependencies>
        <!-- lealone orm 框架 -->
        <dependency>
            <groupId>com.lealone.plugins</groupId>
            <artifactId>lealone-orm</artifactId>
            <version>6.0.0</version>
        </dependency>

        <!-- lealone 微服务框架 -->
        <dependency>
            <groupId>com.lealone.plugins</groupId>
            <artifactId>lealone-service</artifactId>
            <version>6.0.0</version>
        </dependency>
	
        <!-- 使用 vertx 作为 lealone 的 http server -->
        <dependency>
            <groupId>com.lealone.plugins</groupId>
            <artifactId>lealone-vertx</artifactId>
            <version>6.0.0</version>
        </dependency>
	
	<!-- 启用对 Python 的支持 -->
        <dependency>
            <groupId>com.lealone.plugins</groupId>
            <artifactId>lealone-python</artifactId>
            <version>6.0.0</version>
        </dependency>
    </dependencies>
```


### 2. 建表

放在 sql/tables.sql 文件中

```sql
-- 创建表: user，会生成一个名为 User 的模型类
create table if not exists user (
  id long auto_increment primary key,
  name varchar,
  age int
)
package 'com.lealone.examples.python' -- User 类所在的包名
generate code './src/main/java' -- User 类的源文件所在的根目录
```

这一步用于创建 user 表，加了新的扩展语法，
如果指定 generate code，会生成一个名为 User 的模型类：

```java
/**
 * Model for table 'USER'.
 *
 * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.
 */
public class User extends Model<User> {

    public static final User dao = new User(null, ROOT_DAO);

    public final PLong<User> id;
    public final PString<User> name;
    public final PInteger<User> age;

    public User() {
        this(null, REGULAR_MODEL);
    }
```

User 模型类生成的代码是不用修改的，采用的是一种简化的充血模型，
可以基于模型类的字段来构建出跟普通 SQL 极其相似的类型安全的 DSL，下文会有用法演示。



### 3. 创建后端服务

放在 sql/services.sql 文件中

```sql
-- 创建服务: hello_service，并指定用 hello_service.py 实现
create service if not exists hello_service (
  hello(name varchar) varchar
)
language 'python' implement by './python/hello_service.py';


-- 创建服务: user_service，并指定用 user_service.py 实现
create service if not exists user_service (
  add_user(name varchar, age int) long,
  find_by_name(name varchar) user -- 返回的是一个 user 对象
)
language 'python' implement by './python/user_service.py';
```

这一步用于描述一个服务的相关信息，比如它有哪些可调用的方法，并指定服务实现相关的 python 文件。
通过 create service 语句创建的服务在内部会被托管，
服务的注册与发现功能已经内置在框架当中，不需要再依赖其他第三方组件。



### 4. 实现后端服务

放在 python/hello_service.py 文件中

```python
def hello(name):
    return "Python hello " + name
```

放在 python/user_service.py 文件中

```python
import java

# 使用 java 的类
User = java.type('com.lealone.examples.python.User')

def addUser(name, age):
    # 创建 User 对象
    user = User()

    # 如果 name = 'zhh', age = 18
    # 对应的sql是: insert into user(name, age) values('zhh', 18);
    return user.name.set(name).age.set(age).insert() # 链式调用，insert()返回新增记录的rowId

def findByName(name):
    # 如果 name = 'zhh'
    # 对应的sql是: select * from user where name = 'zhh' limit 1
    return User.dao.where().name.eq(name).findOne()

```
服务实现类就是最普通的 Python 代码，服务框架对服务实现类是无侵入的。

这里也演示了 Lealone ORM 框架的基本用法，ORM 框架对应用代码也是无侵入的。

Lealone ORM 框架的更多用法演示可以尝试一下这个
[lealone-orm-demo](https://github.com/lealone/Lealone-Examples/tree/main/orm-demo)

[Lealone ORM 框架快速入门](https://github.com/lealone/Lealone-Examples/blob/main/docs/Lealone%20ORM%E6%A1%86%E6%9E%B6%E5%BF%AB%E9%80%9F%E5%85%A5%E9%97%A8.md)



### 5. 在前端使用后端服务

```html
<!doctype html>
<html>

<head>
    <script src="axios.min-0.21.1.js"></script>
    <script src="lealone-rpc-5.0.0.js"></script>
    <script src="vue.min-2.3.3.js"></script>
</head>

<body lang="en">
    <h1>js full stack test</h1>

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
                    that.message = "user: " + JSON.stringify(user);
                });
            }
        }
    })
    </script>
</body>
</html>
```

lealone-rpc-5.0.0.js 相当于一个 RPC 框架的客户端，通过 axios 与后端 RPC 框架通信，
通过 lealone.getService() 方法获得一个服务代理后就可以直接调用后端服务了。



### 6. 启动 Lealone 并执行 sql 脚本

```java
package com.lealone.examples.python;

import com.lealone.main.Lealone;

// 在前端调用 hello_service 服务，用以下 url:
// http://localhost:9000/service/hello_service/hello?name=zhh

// 在前端调用 user_service 服务，请在浏览器中打开下面这个 url 进行测试:
// http://localhost:9000/fullStack.html
public class PythonDemo {

    public static void main(String[] args) {
        Lealone.main(args, () -> runScript());
    }

    public static void runScript() {
        String url = "jdbc:lealone:tcp://localhost:9210/lealone?user=root";
        // 执行建表脚本，同时自动生成对应的模型类的代码
        // 执行服务创建脚本，同时自动生成对应的服务接口代码
        Lealone.runScript(url, "./sql/tables.sql", "./sql/services.sql");
    }
}
```


### 7. 安装 GraalVM

运行 Python 应用需要事先安装 GraalVM，目前不支持 Windows

安装 GraalVM 请参考 https://www.graalvm.org/22.0/docs/getting-started/

GraalVM 可以直接替换 JDK

这里假设安装后的目录是 /home/test/graalvm-ee-java17-22.0.0.2

接着配置一下 JAVA_HOME 和 PATH 环境变量

export JAVA_HOME=/home/test/graalvm-ee-java17-22.0.0.2

export PATH=$JAVA_HOME/bin:$PATH

最后还需要安装 Python 组件

gu install python

更多信息参考 https://www.graalvm.org/22.0/reference-manual/python/


### 8. 测试服务

需要先用 GraalVM 替换 IDE 中的 JVM，然后在 IDE 中直接运行 PythonDemo

在前端调用 hello_service 服务，用以下 url:

http://localhost:9000/service/hello_service/hello?name=zhh

在前端调用 user_service 服务，请在浏览器中打开下面这个 url 进行测试:

http://localhost:9000/fullStack.html



### 9. 完整例子

下载项目 [python-fullstack-demo](https://github.com/lealone/Lealone-Examples/tree/main/python-demo)

打包 build -p (或: mvn package -Dmaven.test.skip=true)

运行 build -r (或: java -jar ./target/lealone-python-demo-all-1.0.0.jar)


