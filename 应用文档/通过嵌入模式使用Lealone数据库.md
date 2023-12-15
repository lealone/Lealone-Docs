
Lealone 可以当一个嵌入式数据库使用，通过标准 JDBC 访问嵌入式数据库，无需学习 Lealone 的任何 API。

文章最后有完整的项目代码，可直接下载到本机上尝试。

### 在应用的 pom.xml 中增加依赖

```xml
    <dependencies>
        <dependency>
            <groupId>org.lealone</groupId>
            <artifactId>lealone-main</artifactId>
            <version>5.2.1</version>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.1</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```


### 通过标准 JDBC 访问嵌入式数据库

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Assert;

public class EmbedDbDemo {

    public static void main(String[] args) throws Exception {
        // 访问lealone数据库
        crud("jdbc:lealone:embed:lealone");

        // 自动创建mydb1数据库
        crud("jdbc:lealone:embed:mydb1");

        // 自动创建mydb2数据库，但是不持久化
        crud("jdbc:lealone:embed:mydb2?persistent=false");
    }

    // 通过标准JDBC接口进行CRUD操作
    public static void crud(String url) throws Exception {
        Connection conn = DriverManager.getConnection(url, "root", "");
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS test");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS test (f1 int primary key, f2 long)");
        stmt.executeUpdate("INSERT INTO test(f1, f2) VALUES(1, 1)");
        stmt.executeUpdate("UPDATE test SET f2 = 2 WHERE f1 = 1");
        ResultSet rs = stmt.executeQuery("SELECT * FROM test");
        Assert.assertTrue(rs.next());
        System.out.println("f1=" + rs.getInt(1) + " f2=" + rs.getLong(2));
        Assert.assertFalse(rs.next());
        rs.close();
        stmt.executeUpdate("DELETE FROM test WHERE f1 = 1");
        rs = stmt.executeQuery("SELECT * FROM test");
        Assert.assertFalse(rs.next());
        rs.close();
        stmt.close();
        conn.close();
    }
}
```


### 完整例子

下载项目 [embed-db-demo](https://github.com/lealone/Lealone-Examples/tree/main/embed-db-demo)


### 可能出现的问题

如果找不到 Lealone 的依赖包，
需要下载以下项目的代码: 

[lealone-database](https://github.com/lealone/Lealone)


执行 build -i 把它们安装到本地的 maven 仓库即可。

