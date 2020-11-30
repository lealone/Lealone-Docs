Lealone ORM 是一个简洁优雅的类型安全的 ORM 框架，支持链式(或流式) API，能杜绝 SQL 注入。

在建表时，Lealone ORM 可以为每个表自动生成对应的模型类代码，这些代码不需要应用开发人员修改维护。

<b>应用开发人员负责编写的代码无需导入 Lealone ORM 的任何东西(比如类、接口、注解)。</b>



下文通过一个例子来介绍 Lealone ORM 大多数常用的功能，比如单表查添删改操作、多表关联查询、事务操作等等。

该例子创建了 customer 和 order 两张表，它们两者存在关联关系。

文章最后有完整的 Java 代码。

### 1. 建表

```sql
set @packageName 'org.lealone.examples.orm.generated'; -- 生成的模型类所在的包名
set @srcPath './src/main/java'; -- 生成的模型类对应的源文件所在的根目录

-- 创建customer表，会生成一个名为Customer的模型类
create table if not exists customer (
  id long primary key,
  name char(10),
  notes varchar,
  phone int
) package @packageName generate code @srcPath;

-- 创建order表，会生成一个名为Order的模型类
-- order是关键字，所以要用特殊方式表式
create table if not exists `order` (
  customer_id long,
  order_id int primary key,
  order_date date,
  total double,
  FOREIGN KEY(customer_id) REFERENCES customer(id)
) package @packageName generate code @srcPath;

```

这一步用于创建 customer 和 order 表，加了新的扩展语法，
如果指定 generate code，会生成 Customer 和 Order 两个模型类：

```java
public class Customer extends Model<Customer> {

    public static final Customer dao = new Customer(null, ROOT_DAO);

    public final PLong<Customer> id;
    public final PString<Customer> name;
    public final PString<Customer> notes;
    public final PInteger<Customer> phone;
```

生成的 Customer 模型类的代码是不用修改的，采用的是一种简化的充血模型。

可以基于模型类的字段来构建出跟普通 SQL 极其相似的类型安全的 DSL。

因为 customer 和 order 两张表存在关联关系，所以生成的代码也体现了这种关系：

```java
    public Customer addOrder(Order m) {
        m.setCustomer(this);
        super.addModel(m);
        return this;
    }

    public Customer addOrder(Order... mArray) {
        for (Order m : mArray)
            addOrder(m);
        return this;
    }

    public List<Order> getOrderList() {
        return super.getModelList(Order.class);
    }
```

应用开发人员不需要去了解 Customer 模型类的代码实现细节，
只需要通过 IDE 的智能提示功能看看它提供了哪些可用字段和方法即可。


### 2. 单表 crud 操作

#### 2.1 insert

```java
        Customer c = new Customer();
        c.id.set(1001);
        c.name.set("rob");
        c.phone.set(12345678);
        c.insert();
```

或者使用更简洁的链式风格:

```java
        new Customer().id.set(1001).name.set("rob").phone.set(12345678).insert();
```

自动生成的 Customer 类不是 POJO，所以并没用使用常规的 setter/getter 方法，
而是用 c.name.set("rob") 替换 c.setName("rob")，这样的设计是有特别考量的。

Customer 类继承自 org.lealone.orm.Model，因为继承有先天的缺陷，
比如将来在 Model 这个父类中新增一些方法就有可能跟它的子类出现方法名冲突，
所以 Customer 类这样的 Model 子类使用 final 字段比使用 setter/getter 更安全，Model 类将来更容易扩展和维护。

除了继承原因外，使用 final 字段更灵活，比如可以使用 c.name.startsWith("rob") 作为查询条件，可以扩展出无数的方法。


#### 2.2 find

```java
        // find one
        Customer c = Customer.dao.where().id.eq(1001).findOne();

        // find list
        List<Customer> list = Customer.dao.id.eq(1001).findList();
```

在生成的模型类代码里面会有一个 static final 的名为 dao 的字段，通过 dao 来构建链式风格的查询语句。

where() 是可选的，加上去能让代码更易读，一看跟在它后面的调用代码就知道都是用来构建查询条件的。


#### 2.3 update

```java
        // 单记录 update
        c.notes.set("test").update();

        // 批量 update
        Customer.dao.notes.set("batch update").where().name.startsWith("rob").update();
```

单记录 update 是针对通过 find 方法返回的实例的，批量 update 需要通过 dao 来构建 update 语句。


#### 2.4 delete

```java
        // 单记录 delete
        c.delete();

        // 批量 delete
        Customer.dao.where().name.startsWith("rob").delete();
```

跟 update 一样，单记录 delete 也是针对通过 find 方法返回的实例的，批量 delete 需要通过 dao 来构建 delete 语句。


#### 2.5 分页查询

```java
        // count
        int count = dao.findCount();

        // offset、limit
        List<Customer> list = dao.offset(0).limit(1).findList();
```

注意，offset 是从0开始的。


### 3. 关联查询

#### 3.1 批量增加有关联的记录

```java
        Order o1 = new Order().orderId.set(2001).orderDate.set("2018-01-01");
        Order o2 = new Order().orderId.set(2002).orderDate.set("2018-01-01");

        Customer customer = new Customer().id.set(1002).name.set("customer1");
        customer.addOrder(o1, o2).insert();

        // 调用addOrder后，Order的customerId字段会自动对应Customer的id字段
        assertEquals(o1.customerId.get(), customer.id.get());
```

#### 3.2 两表join

```java
        Customer c = Customer.dao;
        Order o = Order.dao;

        customer = c.join(o)
	           .on().id.eq(o.customerId)
	           .where().id.eq(1002)
		   .findOne();

        // 一个customer对应两个Order
        List<Order> orderList = customer.getOrderList();
        assertEquals(2, orderList.size());
        assertTrue(customer == orderList.get(0).getCustomer());
```

Lealone ORM 框架实现关联查询时不会像其他 ORM 框架那样产生 N + 1 问题，
会根据查询语句一次性把满足条件的记录查找出来，然后再创建所有的模型类的实例。


### 4. 事务

```java
        try {
            Customer.dao.beginTransaction(); // 开始一个新事务

            new Customer().id.set(1003).name.set("rob3").insert();
            new Customer().id.set(1004).name.set("rob4").insert();

            Customer.dao.commitTransaction(); // 提交事务
        } catch (Exception e) {
            Customer.dao.rollbackTransaction(); // 回滚事务
            e.printStackTrace();
        }
```

事务默认采用的是自动提交模式，调用 beginTransaction() 后事务变成手动提交模式。


### 5. 完整例子

下载项目 [lealone-orm-demo](https://github.com/lealone/Lealone-Examples/tree/main/orm-demo)

源码编译与打包请执行 build -p

运行执行 build -r


### 6. 可能出现的问题

如果执行 build -p 找不到 lealone 的依赖包，
需要下载以下两个项目的代码: 

[lealone-database](https://github.com/lealone/Lealone)

[lealone-plugins](https://github.com/lealone/Lealone-Plugins)

执行 build -i 把它们安装到本地的 maven 仓库即可。


### 7. 更多例子

请看 [orm unit test](https://github.com/lealone/Lealone/tree/master/lealone-test/src/test/java/org/lealone/test/orm)


### 8. 完整的代码例子

```java
package org.lealone.examples.orm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import org.lealone.examples.orm.generated.Customer;
import org.lealone.examples.orm.generated.Order;

public class OrmDemo {

    public static void main(String[] args) throws Exception {
        createTable();
        testCrud();
        testJoin();
        testTransaction();
    }

    // ------------ 以下代码中出现的的 where()都是可选的 ------------

    // 单表crud操作
    static void testCrud() {
        // insert
        new Customer().id.set(1001).name.set("rob").phone.set(12345678).insert();

        Customer dao = Customer.dao;

        // find one
        Customer c = dao.where().id.eq(1001).findOne();
        assertEquals("rob", c.name.get());

        // find list
        List<Customer> list = dao.id.eq(1001).findList();
        assertEquals(1, list.size());

        // count
        int count = dao.findCount();
        assertEquals(1, count);

        // 单记录 update
        c.notes.set("test").update();
        c = dao.where().id.eq(1001).findOne();
        assertEquals("test", c.notes.get());

        // 批量 update
        dao.notes.set("batch update").where().name.startsWith("rob").update();
        c = dao.id.eq(1001).findOne();
        assertEquals("batch update", c.notes.get());

        // 分页查询
        list = dao.offset(0).limit(1).findList();
        assertEquals(1, list.size());

        // delete
        dao.where().id.eq(1001).delete();
        count = dao.findCount();
        assertEquals(0, count);
    }

    // 两表关联查询
    static void testJoin() {
        // 批量增加有关联的记录
        Order o1 = new Order().orderId.set(2001).orderDate.set("2018-01-01");
        Order o2 = new Order().orderId.set(2002).orderDate.set("2018-01-01");
        Customer customer = new Customer().id.set(1002).name.set("customer1");
        customer.addOrder(o1, o2).insert();
        // 调用addOrder后，Order的customerId字段会自动对应Customer的id字段
        assertEquals(o1.customerId.get(), customer.id.get());

        // 关联查询
        Customer c = Customer.dao;
        Order o = Order.dao;
        customer = c.join(o).on().id.eq(o.customerId).where().id.eq(1002).findOne();

        // 一个customer对应两个Order
        List<Order> orderList = customer.getOrderList();
        assertEquals(2, orderList.size());
        assertTrue(customer == orderList.get(0).getCustomer());
    }

    // 测试事务
    static void testTransaction() {
        try {
            Customer.dao.beginTransaction(); // 开始一个新事务

            new Customer().id.set(1003).name.set("rob3").insert();
            new Customer().id.set(1004).name.set("rob4").insert();

            Customer.dao.commitTransaction(); // 提交事务
        } catch (Exception e) {
            Customer.dao.rollbackTransaction(); // 回滚事务
            e.printStackTrace();
        }

        int count = Customer.dao.where().id.eq(1003).or().id.eq(1004).findCount();
        assertEquals(2, count);
    }

    // 执行建表脚本，同时自动生成对应的模型类的代码
    static void createTable() throws Exception {
        String jdbcUrl = "jdbc:lealone:embed:test";
        System.setProperty("lealone.jdbc.url", jdbcUrl);

        try (Connection conn = DriverManager.getConnection(jdbcUrl); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("RUNSCRIPT FROM './src/main/resources/tables.sql'");
        }
    }
}
```