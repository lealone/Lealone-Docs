使用 Lealone 开发单机微服务应用跟开发分布式微服务应用都很简单，应用从单机变成分布式不需要修改任何代码。

下文通过开发一个 HelloService 一步步演示如何创建和部署一个微服务应用。



## 0. 编译运行需要

* Git 2.x
* JDK 1.8+
* Maven 3.3+
* Eclipse 2018-09 (4.9.0) 或其他支持 Java 1.8 的 IDE



## 1. 使用 Lealone 的微服务应用脚手架创建应用

打开一个命令行窗口，输入以下命令下载脚手架:

git clone https://github.com/lealone/Lealone-CreateApp.git lealone-create-app

然后在当前目录下创建一个 hello 应用：

java -jar lealone-create-app/bin/lealone-create-app-1.0.0.jar -groupId my.app.hello -artifactId hello

应用脚手架的参数用法可以阅读 lealone-create-app/README 文件



## 2. 在 IDE (Eclipse) 中开发运行微服务应用

打开 IDE (Eclipse)，然后以 Maven 项目的方式导入 hello 应用。

当 hello 应用依赖的 jar 包自动下载完成后，用 debug 的方式运行以下 java 文件:

hello-test/src/test/java/my/app/hello/test/HelloTest.java

启动过程中，如果看到包含 "Total time" 的日志，就说明 hello 应用启动成功了。

在浏览器中打开 http://localhost:9000/ 能看到一个静态页面。



## 3. 创建和运行第一个微服务

打开 hello-service/src/main/resources/services.sql 文件，在里面添加如下代码(默认已经有一个了):

```sql
create service if not exists hello_service (
  hello(name varchar) varchar
)
```

然后在 IDE 中运行 hello-test/src/test/java/my/app/hello/test/HelloSqlScriptTest.java

接着刷新一下 hello-service 子项目，会看到在 hello-service/src/main/java/my/app/hello/service 包中多了一些 java 源文件

此时，如果在浏览器中找开 http://localhost:9000/service/hello_service/hello?name=zhh 会发现返回 null，
因为 create service 语句只是定义了一个服务接口，需要实现它。

在 IDE 中打开文件: hello-service/src/main/java/my/app/hello/service/HelloServiceImpl.java

把 hello 方法的实现改成如下方式:

```java
    @Override
    public String hello(String name) {
        return "hello " + name + ", now: " + new Date();
    }
```

在浏览器中刷新一下 http://localhost:9000/service/hello_service/hello?name=zhh

最后就能看到类似这样的结果了: hello zhh, now: Thu May 20 16:36:48 CST 2021

至此，通过 IDE (Eclipse) 开发测试微服务的工作就告一段落了。



下面是如何脱离 IDE 打包部署运行微服务


## 4. 打包 hello 微服务应用

打开一个命令行窗口，切换到 hello 应用的根目录，执行 build -p 就能对应用进行打包了。

打包的结果放在 target 目录中，有以下三种打包格式:
hello-1.0.0 (可以直接运行)<\br>
hello-1.0.0.tar.gz <\br>
hello-1.0.0.zip <\br>



## 5. 部署单机版的 hello 微服务应用

打开两个命令行窗口，都切换到 target\hello-1.0.0\bin 目录

在第一个窗口中执行 lealone 命令启动 hello 应用

在第二个窗口中执行以下命令打开一个 SQL Shell：

sqlshell -url jdbc:lealone:tcp://localhost:9210/lealone -user root

然后在 SQL Shell 中执行 "create database if not exists hello;" 创建 hello 数据库

接着按 quit 退出 SQL Shell。

最后再运行 runSqlScript 即可创建 hello 微服务

runSqlScript 用来执行 target\hello-1.0.0\sql 目录中的所有 sql 脚本。

在浏览器中打开 http://localhost:9000/service/hello_service/hello?name=zhh 就能看到服务调用结果了。



## 6. 部署分布式 hello 微服务应用

#### 6.1 启动集群

先按 Ctrl+C 关掉第 5 小节的 hello 应用

如果是 windows 操作系统，可以在第一个窗口中执行以下命令启动一个带有 3 个节点的集群:

lealone -nodes 3

非 windows 操作系统，可以打开三个新的命令行窗口，都切换到 target\hello-1.0.0\bin 目录，然后在每个窗口中分别运行如下三条命令:

java -Dlealone.logdir=../logs/cluster/node1 -cp ../conf:../lib/* org.lealone.main.Lealone -cluster 1

java -Dlealone.logdir=../logs/cluster/node2 -cp ../conf:../lib/* org.lealone.main.Lealone -cluster 2

java -Dlealone.logdir=../logs/cluster/node3 -cp ../conf:../lib/* org.lealone.main.Lealone -cluster 3



#### 6.2 创建数据库和微服务

打开一个命令行窗口，切换到 target\hello-1.0.0\bin 目录，执行以下命令打开一个 SQL Shell：

sqlshell -url jdbc:lealone:tcp://localhost:9210/lealone -user root

然后在 SQL Shell 中执行 "create database if not exists hello run mode replication;" 创建 hello 数据库

这里的 run mode replication 是关键，默认是 3 副本的，表示在 hello 数据库创建的微服务会部署在 3 个节点上。

接着按 quit 退出 SQL Shell。

最后再运行 runSqlScript 即可创建 hello 微服务

在浏览器中打开 http://localhost:9000/service/hello_service/hello?name=zhh 就能看到服务调用结果了。

如果把 localhost 改成 127.0.0.1、127.0.0.2、127.0.0.3 都是可以的，
刷新一次浏览器会在对应的命令行窗口中看到一条服务调用日志，类似这样: 

INFO 16:36:48.860 execute service: hello.public.hello_service



## 7. 使用 nginx 和 lealone 共同搭建一个分布式微服务集群

在 6.2 小节中，如果在浏览器中只想通过一个统一的主机名访问后端的微服务要怎么实现呢？

通过 nginx 的反向代理和负载均衡就能做到，按以下方式配置一下 nginx.conf 文件即可:

```java
http {
     
    #……省略nginx的其他默认配置……

    upstream lealone {
        server 127.0.0.1:9000;
	server 127.0.0.2:9000;
	server 127.0.0.3:9000;
    }

    #……省略nginx的其他默认配置……

    server { 
        #……省略nginx的其他默认配置……

        location /service {
            proxy_pass http://lealone;
        }

	#……省略nginx的其他默认配置……
    }

    #……省略nginx的其他默认配置……

}
```java

最后在浏览器中就可以通一用 http://localhost/service/hello_service/hello?name=zhh 来调用后端的微服务了

nginx 的负载均衡策略默认是轮询的方式，每刷新一次会看到后端三个 lealone 节点的窗口中轮流产生新的调用日志。

