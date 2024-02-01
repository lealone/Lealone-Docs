在 Spring 现有应用中使用 Lealone 微服务框架。

文章最后有完整的项目代码，可直接下载到本机上尝试。


### 在 pom.xml 中增加依赖

```xml
    <dependencies>
        <dependency>
            <groupId>com.lealone.plugins</groupId>
            <artifactId>lealone-spring</artifactId>
            <version>5.2.0</version>
        </dependency>
    </dependencies>
```


### 在 @SpringBootApplication 中增加 scanBasePackages 扫描 com.lealone.plugins.spring 的组件

```java
@SpringBootApplication(scanBasePackages = {
        "com.lealone.plugins.spring",
        "com.lealone.examples.spring" })
public class SpringDemo {

    public static void main(String[] args) {
        HelloService.create();
        SpringApplication.run(SpringDemo.class, args);
    }
}
```

### 创建应用的 RestController

```java
@RestController
public class HelloController {

    // 用这样的url打开: http://localhost:8080/hello?name=zhh
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello " + name;
    }
}
```

### 创建 HelloService

```java
public class HelloService {

    // 用这样的url打开: http://localhost:8080/service/hello_service/hello?name=zhh
    public String hello(String name) {
        return "Hello " + name;
    }

    public static void create() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:lealone:embed:lealone", "root", "");
            String sql = "create service if not exists hello_service (hello(name varchar) varchar)" //
                    + " implement by '" + HelloService.class.getName() + "'";
            conn.createStatement().executeUpdate(sql);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```


### 运行

mvn spring-boot:run


在浏览器中打开下面的 URL 进行测试:

http://localhost:8080/hello?name=zhh

http://localhost:8080/service/hello_service/hello?name=zhh



### 完整例子

下载项目 [spring-demo](https://github.com/lealone/Lealone-Examples/tree/main/spring-demo)



### 可能出现的问题

如果执行 build -p 找不到 Lealone 的依赖包，
需要下载以下项目的代码: 

[lealone-database](https://github.com/lealone/Lealone)


执行 build -i 把它们安装到本地的 maven 仓库即可。

