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


create table if not exists user (
    user_id varchar(25) not null,
    password varchar(25)  not null,
    roles varchar(100),
    constraint pk_user primary key (user_id)
);

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
);

create table if not exists profile (
    user_id varchar(25) not null,
    language_preference varchar(80),
    favorite_category_id varchar(30),
    list_option int,
    banner_opttion int,
    constraint pk_profile primary key (user_id)
);

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
);

create table if not exists banner_data (
    favcategory varchar(80) not null,
    banner_name varchar(255)  null,
    constraint pk_banner_data primary key (favcategory)
);

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
);

create table if not exists order_status (
    orderid int not null,
    order_itemid int not null,
    timestamp date not null,
    status varchar(2) not null,
    constraint pk_order_status primary key (orderid, order_itemid)
);

create table if not exists order_item (
    orderid int not null,
    order_itemid int not null,
    itemid varchar(10) not null,
    quantity int not null,
    unitprice decimal(10,2) not null,
    constraint pk_order_item primary key (orderid, order_itemid)
);

create table if not exists category (
    catid varchar(10) not null,
    name varchar(80) null,
    logo varchar(80) null,
    descn varchar(255) null,
    constraint pk_category primary key (catid)
);

create table if not exists product (
    productid varchar(10) not null,
    categoryid varchar(10) not null,
    name varchar(80) null,
    logo varchar(80) null,
    descn varchar(255) null,
    constraint pk_product primary key (productid),
    constraint fk_product_1 foreign key (categoryid) references category (catid)
);

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
);

create index if not exists item_prod on item (productid);

create table if not exists inventory (
    itemid varchar(10) not null,
    qty int not null,
    constraint pk_inventory primary key (itemid)
);

create table if not exists sequence (
    name varchar(30) not null,
    nextid int not null,
    constraint pk_sequence primary key (name)
);


INSERT INTO user VALUES('j2ee','j2ee',null);
INSERT INTO user VALUES('admin','admin','admin');

INSERT INTO account VALUES('j2ee','yourname@yourdomain.com','ABC', 'XYX', 'OK', '901 San Antonio Road', 'MS UCUP02-206', 'Palo Alto', 'CA', '94303', 'US',  '555-555-5555', '1234567', 'Visa', '2005-12-15');
INSERT INTO account VALUES('admin','yourname@yourdomain.com','ABC', 'XYX', 'OK', '901 San Antonio Road', 'MS UCUP02-206', 'Palo Alto', 'CA', '94303', 'US',  '555-555-5555', '1234567', 'Visa', '2005-12-15');

INSERT INTO profile VALUES('j2ee','english','DOGS',1,1);
INSERT INTO profile VALUES('admin','english','DOGS',1,1);

INSERT INTO sequence VALUES('ordernum', 1000);
INSERT INTO sequence VALUES('orderitemnum', 1000);

INSERT INTO banner_data VALUES ('FISH','<image src="../images/banner_fish.gif">');
INSERT INTO banner_data VALUES ('CATS','<image src="../images/banner_cats.gif">');
INSERT INTO banner_data VALUES ('DOGS','<image src="../images/banner_dogs.gif">');
INSERT INTO banner_data VALUES ('REPTILES','<image src="../images/banner_reptiles.gif">');
INSERT INTO banner_data VALUES ('BIRDS','<image src="../images/banner_birds.gif">');

INSERT INTO category VALUES ('FISH','Fish','cat_fish.jpg','Fish');
INSERT INTO category VALUES ('DOGS','Dogs','cat_dog.jpg','Dogs');
INSERT INTO category VALUES ('REPTILES','Reptiles','cat_reptile.jpg','Reptiles');
INSERT INTO category VALUES ('CATS','Cats','cat_cat.jpg','Cats');
INSERT INTO category VALUES ('BIRDS','Birds','cat_bird.jpg','Birds');

INSERT INTO product VALUES ('FI-SW-01','FISH','Angelfish','fish1.jpg','Salt Water fish from Australia');
INSERT INTO product VALUES ('FI-SW-02','FISH','Tiger Shark','fish4.jpg','Salt Water fish from Australia');
INSERT INTO product VALUES ('FI-FW-01','FISH', 'Koi','fish3.jpg','Fresh Water fish from Japan');
INSERT INTO product VALUES ('FI-FW-02','FISH', 'Goldfish','fish2.jpg','Fresh Water fish from China');
INSERT INTO product VALUES ('K9-BD-01','DOGS','Bulldog','dog2.jpg','Friendly dog from England');
INSERT INTO product VALUES ('K9-PO-02','DOGS','Poodle','dog6.jpg','Cute dog from France');
INSERT INTO product VALUES ('K9-DL-01','DOGS', 'Dalmation','dog5.jpg','Great dog for a Fire Station');
INSERT INTO product VALUES ('K9-RT-01','DOGS', 'Golden Retriever','dog1.jpg','Great family dog');
INSERT INTO product VALUES ('K9-RT-02','DOGS', 'Labrador Retriever','dog5.jpg','Great hunting dog');
INSERT INTO product VALUES ('K9-CW-01','DOGS', 'Chihuahua','dog4.jpg','Great companion dog');
INSERT INTO product VALUES ('RP-SN-01','REPTILES','Rattlesnake','lizard3.jpg','Doubles as a watch dog');
INSERT INTO product VALUES ('RP-LI-02','REPTILES','Iguana','lizard2.jpg','Friendly green friend');
INSERT INTO product VALUES ('FL-DSH-01','CATS','Manx','cat3.jpg','Great for reducing mouse populations');
INSERT INTO product VALUES ('FL-DLH-02','CATS','Persian','cat1.jpg','Friendly house cat, doubles as a princess');
INSERT INTO product VALUES ('AV-CB-01','BIRDS','Amazon Parrot','bird4.jpg','Great companion for up to 75 years');
INSERT INTO product VALUES ('AV-SB-02','BIRDS','Finch','bird1.jpg','Great stress reliever');

INSERT INTO supplier VALUES (1,'XYZ Pets','AC','600 Avon Way','','Los Angeles','CA','94024','212-947-0797');
INSERT INTO supplier VALUES (2,'ABC Pets','AC','700 Abalone Way','','San Francisco ','CA','94024','415-947-0797');

INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-1','FI-SW-01',16.50,10.00,1,'P','Large');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-2','FI-SW-01',16.50,10.00,1,'P','Small');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-3','FI-SW-02',18.50,12.00,1,'P','Toothless');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-4','FI-FW-01',18.50,12.00,1,'P','Spotted');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-5','FI-FW-01',18.50,12.00,1,'P','Spotless');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-6','K9-BD-01',18.50,12.00,1,'P','Male Adult');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-7','K9-BD-01',18.50,12.00,1,'P','Female Puppy');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-8','K9-PO-02',18.50,12.00,1,'P','Male Puppy');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-9','K9-DL-01',18.50,12.00,1,'P','Spotless Male Puppy');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-10','K9-DL-01',18.50,12.00,1,'P','Spotted Adult Female');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-11','RP-SN-01',18.50,12.00,1,'P','Venomless');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-12','RP-SN-01',18.50,12.00,1,'P','Rattleless');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-13','RP-LI-02',18.50,12.00,1,'P','Green Adult');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-14','FL-DSH-01',58.50,12.00,1,'P','Tailless');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-15','FL-DSH-01',23.50,12.00,1,'P','With tail');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-16','FL-DLH-02',93.50,12.00,1,'P','Adult Female');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-17','FL-DLH-02',93.50,12.00,1,'P','Adult Male');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-18','AV-CB-01',193.50,92.00,1,'P','Adult Male');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-19','AV-SB-02',15.50, 2.00,1,'P','Adult Male');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-20','FI-FW-02',5.50, 2.00,1,'P','Adult Male');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-21','FI-FW-02',5.29, 1.00,1,'P','Adult Female');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-22','K9-RT-02',135.50, 100.00,1,'P','Adult Male');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-23','K9-RT-02',145.49, 100.00,1,'P','Adult Female');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-24','K9-RT-02',255.50, 92.00,1,'P','Adult Male');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-25','K9-RT-02',325.29, 90.00,1,'P','Adult Female');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-26','K9-CW-01',125.50, 92.00,1,'P','Adult Male');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-27','K9-CW-01',155.29, 90.00,1,'P','Adult Female');
INSERT INTO item (itemid, productid, listprice, unitcost, supplierid, status, attr1) VALUES('EST-28','K9-RT-01',155.29, 90.00,1,'P','Adult Female');

INSERT INTO inventory (itemid, qty ) VALUES ('EST-1',1);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-2',2);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-3',3);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-4',4);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-5',5);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-6',6);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-7',7);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-8',8);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-9',9);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-10',10);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-11',11);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-12',12);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-13',13);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-14',14);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-15',15);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-16',16);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-17',17);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-18',18);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-19',19);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-20',20);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-21',21);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-22',22);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-23',23);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-24',24);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-25',25);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-26',26);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-27',27);
INSERT INTO inventory (itemid, qty ) VALUES ('EST-28',28);
