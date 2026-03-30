set @llm_provider 'doubao'; --目前只支持doubao
set @llm_model 'doubao-seed-2-0-pro-260215';
set @llm_api_key '替换成你的apikey';

create table if not exists user (
  id long auto_increment primary key,
  name varchar,
  age int
);

create service if not exists user_service (
  add_user(name varchar, age int) long,
  find_by_name(name varchar) user
);
