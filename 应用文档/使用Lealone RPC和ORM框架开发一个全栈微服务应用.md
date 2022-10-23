这里介绍如何使用前端 Vue 框架配合 Lealone 数据库内置的 RPC 和 ORM 框架开发一个简单的全栈微服务应用。

<b>Lealone 的学习成本极低，下文只有启动 HttpServer 的代码需要引入 Lealone 的类，</br>
其他应用开发人员负责编写的代码无需导入 Lealone 的任何东西(比如类、接口、注解)。</b>

文章最后有完整的项目代码，可直接下载到本机上尝试。

### 1. 启动 http server

```java
    public static void startHttpServer() {
        // 通过JDBC访问的数据库的URL
        String jdbcUrl = "jdbc:lealone:embed:test";

        // 静态资源文件的根目录，如果有多个可以用逗号分隔
        String webRoot = "./web";

        // 启动HttpServer，请在浏览器中打开下面这个URL进行测试:
        // http://localhost:8080/fullStack.html
        HttpServer server = HttpServer.create();
        server.setJdbcUrl(jdbcUrl);
        server.setWebRoot(webRoot);
        server.start();
    }
```

### 2. 建表

```sql
-- 创建表: user，会生成一个名为 User 的模型类
create table if not exists user (
  id long auto_increment primary key,
  name varchar,
  age int
)
package 'org.lealone.examples.fullstack.generated.model' -- User 类所在的包名
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

```sql
-- 创建服务: user_service，会生成一个对应的 UserService 接口
create service if not exists user_service (
  add_user(name varchar, age int) long, -- 定义 UserService 接口方法 add_user
  find_by_name(name varchar) user -- 定义 UserService 接口方法 find_by_name
)
package 'org.lealone.examples.fullstack.generated.service' -- UserService 接口所在的包名
implement by 'org.lealone.examples.fullstack.UserServiceImpl' -- UserService 接口的默认实现类
generate code './src/main/java' -- UserService 接口源文件的根目录
```

这一步用于描述一个服务的相关信息，比如它有哪些可调用的方法，
会为每个服务生成一个对应的Java接口，并指定服务接口的默认实现类。
通过 create service 语句创建的服务在内部会被托管，
服务的注册与发现功能已经内置在框架当中，不需要再依赖其他第三方组件。


### 4. 实现后端服务

```java
package org.lealone.examples.fullstack;

import org.lealone.examples.fullstack.generated.model.User;
import org.lealone.examples.fullstack.generated.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public Long addUser(String name, Integer age) {
        // 如果 name = 'zhh', age = 18
        // 对应的sql是: insert into user(name, age) values('zhh', 18);
        return new User().name.set(name).age.set(age).insert(); // 链式调用，insert()返回新增记录的 rowId
    }

    @Override
    public User findByName(String name) {
        // 如果 name = 'zhh'
        // 对应的 sql 是: select * from user where name = 'zhh' limit 1
        return User.dao.where().name.eq(name).findOne();
    }
}
```

服务实现类就是一个最普通的接口实现类，服务框架对服务实现类是无侵入的。

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
    <h1>full stack test</h1>

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

lealone-rpc-5.0.0.js 相当于一个 RPC 框架的客户端，通过 axios 与后端 RPC 框架通信，
通过 lealone.getService() 方法获得一个服务代理后就可以直接调用后端服务了。



### 6. 完整例子

下载项目 [lealone-fullstack-demo](https://github.com/lealone/Lealone-Examples/tree/main/fullstack-demo)

源码编译与打包请执行 build -p

运行执行 build -r

启动成功后，在浏览器中打开下面这个 URL 进行测试:
http://localhost:8080/fullStack.html


### 7. 可能出现的问题

如果执行 build -p 找不到 Lealone 的依赖包，
需要下载以下项目的代码: 

[lealone-database](https://github.com/lealone/Lealone)


执行 build -i 把它们安装到本地的 maven 仓库即可。

