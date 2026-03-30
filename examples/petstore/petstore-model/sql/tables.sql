drop index if exists product_cat;
drop index if exists product_name;
drop index if exists item_prod;


drop table if exists user;
drop table if exists account;
drop table if exists profile;
drop table if exists supplier;
drop table if exists banner_data;
drop table if exists orders;
drop table if exists order_status;
drop table if exists order_item;
drop table if exists category;
drop table if exists product;
drop table if exists item;
drop table if exists inventory;
drop table if exists sequence;


set @packageName 'com.lealone.examples.petstore.model'; -- 生成的模型类所在的包名
--set @srcPath '../petstore-model/src/main/java'; -- 生成的模型类对应的源文件所在的根目录


create table if not exists user (
    user_id varchar(25) not null,
    password varchar(25)  not null,
    roles varchar(100),

    constraint pk_user primary key (user_id)
) package @packageName generate code @srcPath;

create table if not exists account (
    user_id varchar(25) not null,
    email varchar(80),
    first_name varchar(80),
    last_name varchar(80),
    status varchar(2),
    address_1 varchar(80),
    address_2 varchar(40),
    city varchar(80),
    state varchar(80),
    zip varchar(20),
    country varchar(20),
    phone varchar(80),
    credit_card_number varchar(50),
    credit_card_type varchar(20),
    credit_card_expiry date,

    constraint pk_account primary key (user_id)
) package @packageName generate code @srcPath;

create table if not exists profile (
    user_id varchar(25) not null,
    language_preference varchar(80),
    favorite_category_id varchar(30),
    list_option int,
    banner_opttion int,

    constraint pk_profile primary key (user_id)
) package @packageName generate code @srcPath;

create table if not exists supplier (
    suppid int not null,
    name varchar(80) null,
    status varchar(2) not null,
    addr1 varchar(80) null,
    addr2 varchar(80) null,
    city varchar(80) null,
    state varchar(80) null,
    zip varchar(5) null,
    phone varchar(80) null,
    constraint pk_supplier primary key (suppid)
) package @packageName generate code @srcPath;

create table if not exists banner_data (
    favcategory varchar(80) not null,
    banner_name varchar(255)  null,
    constraint pk_banner_data primary key (favcategory)
) package @packageName generate code @srcPath;

create table if not exists orders (
    orderid int not null,
    userid varchar(80) not null,
    orderdate date not null,
    shipaddr1 varchar(80) not null,
    shipaddr2 varchar(80) null,
    shipcity varchar(80) not null,
    shipstate varchar(80) not null,
    shipzip varchar(20) not null,
    shipcountry varchar(20) not null,
    billaddr1 varchar(80) not null,
    billaddr2 varchar(80)  null,
    billcity varchar(80) not null,
    billstate varchar(80) not null,
    billzip varchar(20) not null,
    billcountry varchar(20) not null,
    courier varchar(80) not null,
    totalprice decimal(10,2) not null,
    billtofirstname varchar(80) not null,
    billtolastname varchar(80) not null,
    shiptofirstname varchar(80) not null,
    shiptolastname varchar(80) not null,
    creditcard varchar(80) not null,
    exprdate varchar(7) not null,
    cardtype varchar(80) not null,
    locale varchar(80) not null,
    constraint pk_orders primary key (orderid)
) package @packageName generate code @srcPath;

create table if not exists order_status (
    orderid int not null,
    order_itemid int not null,
    timestamp date not null,
    status varchar(2) not null,
    constraint pk_order_status primary key (orderid, order_itemid)
) package @packageName generate code @srcPath;

create table if not exists order_item (
    orderid int not null,
    order_itemid int not null,
    itemid varchar(10) not null,
    quantity int not null,
    unitprice decimal(10,2) not null,
    constraint pk_order_item primary key (orderid, order_itemid)
) package @packageName generate code @srcPath;

create table if not exists category (
	catid varchar(10) not null,
	name varchar(80) null,
	logo varchar(80) null,
	descn varchar(255) null,
	constraint pk_category primary key (catid)
) package @packageName generate code @srcPath;

create table if not exists product (
    productid varchar(10) not null,
    categoryid varchar(10) not null,
    name varchar(80) null,
    logo varchar(80) null,
    descn varchar(255) null,
    constraint pk_product primary key (productid),
    constraint fk_product_1 foreign key (categoryid) references category (catid)
) package @packageName generate code @srcPath;

create index if not exists product_cat on product (categoryid);
create index if not exists product_name on product (name);

create table if not exists item (
    itemid varchar(10) not null,
    productid varchar(10) not null,
    listprice decimal(10,2) null,
    unitcost decimal(10,2) null,
    supplierid int null,
    status varchar(2) null,
    attr1 varchar(80) null,
    attr2 varchar(80) null,
    attr3 varchar(80) null,
    attr4 varchar(80) null,
    attr5 varchar(80) null,
    constraint pk_item primary key (itemid),
    constraint fk_item_1 foreign key (productid) references product (productid),
    constraint fk_item_2 foreign key (supplierid) references supplier (suppid)
) package @packageName generate code @srcPath;

create index if not exists item_prod on item (productid);

create table if not exists inventory (
    itemid varchar(10) not null,
    qty int not null,
    constraint pk_inventory primary key (itemid)
) package @packageName generate code @srcPath;

create table if not exists sequence (
    name varchar(30) not null,
    nextid int not null,
    constraint pk_sequence primary key (name)
) package @packageName generate code @srcPath;

