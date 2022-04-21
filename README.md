# 前言

Lealone 既可以作为一个独立的数据库来使用，也可以当成一个微服务框架来用，或者两者同时用。


# 目录

# Lealone 应用文档

### Lealone 数据库

#### 用户文档

  * [快速入门](https://github.com/lealone/Lealone-Docs/blob/master/%E5%BA%94%E7%94%A8%E6%96%87%E6%A1%A3/%E7%94%A8%E6%88%B7%E6%96%87%E6%A1%A3.md)

  * [复制集群搭建](https://github.com/lealone/Lealone-Docs/blob/master/%E5%BA%94%E7%94%A8%E6%96%87%E6%A1%A3/%E5%A4%8D%E5%88%B6%E9%9B%86%E7%BE%A4%E6%90%AD%E5%BB%BA.md)

  * [Sharding 集群搭建](https://github.com/lealone/Lealone-Docs/blob/master/%E5%BA%94%E7%94%A8%E6%96%87%E6%A1%A3/Sharding%E9%9B%86%E7%BE%A4%E6%90%AD%E5%BB%BA.md)

  * [从源码构建 Lealone 数据库](https://github.com/lealone/Lealone-Docs/blob/master/%E5%BA%94%E7%94%A8%E6%96%87%E6%A1%A3/%E4%BB%8E%E6%BA%90%E7%A0%81%E6%9E%84%E5%BB%BALealone.md)
 
  * [单机模式、复制集群模式、Sharding 集群模式](https://github.com/lealone/Lealone-Docs/blob/master/%E5%BA%94%E7%94%A8%E6%96%87%E6%A1%A3/%E8%BF%90%E8%A1%8C%E6%A8%A1%E5%BC%8F%E5%BF%AB%E9%80%9F%E5%85%A5%E9%97%A8.md)
  
  * [使用 MySQL 或 PostgreSQL 的客户端和 SQL 语法访问 Lealone 数据库](https://github.com/lealone/Lealone-Docs/blob/master/%E5%BA%94%E7%94%A8%E6%96%87%E6%A1%A3/%E4%BD%BF%E7%94%A8MySQL%E6%88%96PostgreSQL%E7%9A%84%E5%AE%A2%E6%88%B7%E7%AB%AF%E5%92%8CSQL%E8%AF%AD%E6%B3%95%E8%AE%BF%E9%97%AELealone%E6%95%B0%E6%8D%AE%E5%BA%93.md)


#### SQL Reference

  * 因为 Lealone 的 SQL 引擎从 [H2 数据库](http://www.h2database.com/html/main.html) 的 SQL 引擎发展而来， 所以 Lealone 的 SQL 用法与 H2 数据库一样

  * [SQL Grammar](http://www.h2database.com/html/grammar.html)

  * [Functions](http://www.h2database.com/html/functions.html)

  * [Data Types](http://www.h2database.com/html/datatypes.html)


### Lealone 微服务框架

  * [使用 RPC 框架来开发 web 应用](https://github.com/lealone/Lealone-Docs/blob/master/%E5%BA%94%E7%94%A8%E6%96%87%E6%A1%A3/%E4%BD%BF%E7%94%A8RPC%E6%A1%86%E6%9E%B6%E6%9D%A5%E5%BC%80%E5%8F%91web%E5%BA%94%E7%94%A8.md)
  
  * [使用 Lealone 脚手架创建微服务应用](https://github.com/lealone/Lealone-CreateApp)

  * [使用 Lealone 开发单机和分布式微服务应用](https://github.com/lealone/Lealone-Docs/blob/master/%E5%BA%94%E7%94%A8%E6%96%87%E6%A1%A3/%E4%BD%BF%E7%94%A8Lealone%E5%BC%80%E5%8F%91%E5%8D%95%E6%9C%BA%E5%92%8C%E5%88%86%E5%B8%83%E5%BC%8F%E5%BE%AE%E6%9C%8D%E5%8A%A1%E5%BA%94%E7%94%A8.md)

  * [同时使用 Java 和 JavaScript 开发微服务应用](https://github.com/lealone/Lealone-Docs/blob/master/%E5%BA%94%E7%94%A8%E6%96%87%E6%A1%A3/%E5%90%8C%E6%97%B6%E4%BD%BF%E7%94%A8Java%E5%92%8CJavaScript%E5%BC%80%E5%8F%91%E5%BE%AE%E6%9C%8D%E5%8A%A1%E5%BA%94%E7%94%A8.md)


### Lealone ORM 框架

  * [Lealone ORM 框架快速入门](https://github.com/lealone/Lealone-Docs/blob/master/%E5%BA%94%E7%94%A8%E6%96%87%E6%A1%A3/Lealone%20ORM%E6%A1%86%E6%9E%B6%E5%BF%AB%E9%80%9F%E5%85%A5%E9%97%A8.md)
  
  * [使用 Lealone RPC 和 ORM 框架开发一个全栈微服务应用](https://github.com/lealone/Lealone-Docs/blob/master/%E5%BA%94%E7%94%A8%E6%96%87%E6%A1%A3/%E4%BD%BF%E7%94%A8Lealone%20RPC%E5%92%8CORM%E6%A1%86%E6%9E%B6%E5%BC%80%E5%8F%91%E4%B8%80%E4%B8%AA%E5%85%A8%E6%A0%88%E5%BE%AE%E6%9C%8D%E5%8A%A1%E5%BA%94%E7%94%A8.md)


### Lealone 存储引擎

  * [存储引擎快速入门](https://github.com/lealone/Lealone-Docs/blob/master/%E5%BA%94%E7%94%A8%E6%96%87%E6%A1%A3/%E5%AD%98%E5%82%A8%E5%BC%95%E6%93%8E%E5%BF%AB%E9%80%9F%E5%85%A5%E9%97%A8.md)



# Lealone 设计文档

  * [开发文档](https://github.com/lealone/Lealone-Docs/blob/master/%E8%AE%BE%E8%AE%A1%E6%96%87%E6%A1%A3/%E5%BC%80%E5%8F%91%E6%96%87%E6%A1%A3.md)
  
  * [架构图](https://github.com/lealone/Lealone-Docs/blob/master/%E8%AE%BE%E8%AE%A1%E6%96%87%E6%A1%A3/%E8%AE%BE%E8%AE%A1%E6%96%87%E6%A1%A3.md)
  
  * [早期的分布式事务模型](https://github.com/lealone/Lealone-Docs/blob/master/%E8%AE%BE%E8%AE%A1%E6%96%87%E6%A1%A3/%E4%BA%8B%E5%8A%A1/%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1%E6%A8%A1%E5%9E%8B.md)
  
  * [全局快照隔离](https://github.com/lealone/Lealone-Docs/blob/master/%E8%AE%BE%E8%AE%A1%E6%96%87%E6%A1%A3/%E4%BA%8B%E5%8A%A1/%E5%85%A8%E5%B1%80%E5%BF%AB%E7%85%A7%E9%9A%94%E7%A6%BB.md)



