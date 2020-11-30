使用RPC框架来开发web应用，这是什么鬼？RPC框架能开发web应用？

没错，你真的没看错，就是用RPC框架来开发web应用。

解释不清楚的，上车吧，跟着体验一下。

### 1. 启动 http server

```java
    public static void startHttpServer() {
        // 通过JDBC访问的数据库的URL
        String jdbcUrl = "jdbc:lealone:embed:test";

        // 静态资源文件的根目录，如果有多个可以用逗号分隔
        String webRoot = "./web";

        // 启动HttpServer，请在浏览器中打开下面这个URL进行测试:
        // http://localhost:8080/hello.html
        HttpServer server = new HttpServer();
        server.setJdbcUrl(jdbcUrl);
        server.setWebRoot(webRoot);
        server.start();
    }
```

### 2. 创建后端服务

```sql
-- 创建服务: hello_service，会生成一个对应的HelloService接口
create service if not exists hello_service (
  say_hello(name varchar) varchar -- HelloService接口方法定义
)
package 'org.lealone.examples.rpc.generated' -- HelloService接口所在的包名
implement by 'org.lealone.examples.rpc.HelloServiceImpl' -- HelloService接口的默认实现类
generate code './src/main/java' -- HelloService接口源文件的根目录
```

这一步用于描述一个服务的相关信息，比如它有哪些可调用的方法，
会为每个服务生成一个对应的Java接口，并指定服务接口的默认实现类。
通过create service语句创建的服务在内部会被托管，
服务的注册与发现功能已经内置在框架当中，不需要再依赖其他第三方组件。


### 3. 实现后端服务

```java
package org.lealone.demo;

import org.lealone.demo.generated.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
```

服务实现类就是一个最普通的接口实现类，框架对服务实现类是无侵入的。


### 4. 在前端使用后端服务

```html
<!doctype html>
<html>

<head>
    <script src="sockjs-1.1.2.js"></script>
    <script src="lealone-5.0.0.js"></script>
    <script src="vue.min-2.3.3.js"></script>
</head>

<body lang="en">
    <h1>rpc client test</h1>

    <div id="test">
        say hello: <input v-model="name">
        <button v-on:click="sayHello">submit</button>
        <p>{{ message }}</p>
    </div>

    <script> 
    new Vue({
        el: '#test',
        data: {
            name: "zhh",
            message: ""
        },
        methods: {
            sayHello: function() {
                var that = this;
                //获得一个服务代理
                var s = lealone.getService("hello_service");
                //调用服务
                s.sayHello(this.name, function(message) {
                    that.message = message;
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



### 5. 完整例子

下载项目 [lealone-rpc-demo](https://github.com/lealone/Lealone-Examples/tree/main/rpc-demo)

源码编译与打包请执行 build -p

运行执行 build -r

启动成功后，在浏览器中打开下面这个URL进行测试:
http://localhost:8080/hello.html



### 6. 可能出现的问题

如果执行build -p找不到lealone的依赖包，
需要下载以下两个项目的代码: 

[lealone-database](https://github.com/lealone/Lealone)

[lealone-plugins](https://github.com/lealone/Lealone-Plugins)

执行 build -i 把它们安装到本地的maven仓库即可。

