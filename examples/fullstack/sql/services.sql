-- 以下参数也可以通过-D参数或通过环境变量的方式设置
set @llm_provider 'doubao'; --目前只支持doubao
set @llm_model 'doubao-seed-2-0-pro-260215';
-- set @llm_api_key '替换成你的apikey';


-- 删除表
drop table if exists user;

-- 创建表: user，会生成一个名为User的模型类
create table if not exists user (
  id long auto_increment primary key,
  name varchar,
  age int
)
package 'com.lealone.examples.fullstack.generated.model' -- User类所在的包名
generate code './src/main/java' -- User类的源文件所在的根目录


-- 删除服务
drop service if exists user_service;

-- 创建服务: user_service，会生成一个对应的UserService接口
create service if not exists user_service (
  add_user(name varchar, age int) long, -- 定义UserService接口方法 add_user
  find_by_name(name varchar) user -- 定义UserService接口方法 find_by_name
)
package 'com.lealone.examples.fullstack.generated.service' -- UserService接口所在的包名
implement by 'com.lealone.examples.fullstack.UserServiceImpl' -- UserService接口的默认实现类
generate code './src/main/java' -- UserService接口源文件的根目录
