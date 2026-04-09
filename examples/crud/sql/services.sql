set llm (
    provider: 'doubao', --目前只支持doubao
    model: 'doubao-seed-2-0-pro-260215',
    api_key: '替换成你的apikey'
);

create table if not exists user (
  id long auto_increment primary key,
  name varchar,
  age int
);

create service if not exists user_service (
  add_user(name varchar, age int) long,
  find_by_name(name varchar) user
);
