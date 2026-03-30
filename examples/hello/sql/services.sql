-- 删除服务
drop service if exists hello_service;

-- 创建服务: hello_service
create service if not exists hello_service (
  say_hello(name varchar) varchar -- HelloService 方法定义
)
implement by 'com.lealone.examples.rpc.HelloService' -- HelloService 默认实现类
