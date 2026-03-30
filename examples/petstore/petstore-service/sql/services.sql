
-- 删除服务: user_service
drop service if exists user_service;

-- 创建服务: user_service
create service if not exists user_service (
  login(user_id varchar, password varchar) varchar,
  logout(user_id varchar) varchar,
  register(user_id varchar, password varchar, password2 varchar) void,
  update(account Account) void,
  get_user(user_id varchar) varchar
)
implement by 'com.lealone.examples.petstore.service.UserService' -- user_service 的默认实现类
;

-- 删除服务: store_service
drop service if exists store_service;

-- 创建服务: store_service
create service if not exists store_service (
  add_product(product Product) varchar,
  get_all_categories() varchar,
  get_all_product_items(product_id varchar) varchar
)
implement by 'com.lealone.examples.petstore.service.StoreService' -- store_service 的默认实现类
;

-- 删除服务: view_cart_service
drop service if exists view_cart_service;

-- 创建服务: view_cart_service
create service if not exists view_cart_service (
  add_item(cart_id varchar, item_id varchar) void,
  remove_item(cart_id varchar, item_id varchar) void,
  update(cart_id varchar, item_id varchar, quantity int) void,
  get_items(cart_id varchar) varchar
)
implement by 'com.lealone.examples.petstore.service.ViewCartService' -- view_cart_service 的默认实现类
;
