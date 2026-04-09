set llm (
    provider: 'doubao', --目前只支持doubao
    model: 'doubao-seed-2-0-pro-260215',
    api_key: '替换成你的apikey'
);

create service if not exists hello_service (
  say_hello(name varchar) varchar
)
