USE master
GO
IF EXISTS (SELECT name FROM sys.databases WHERE name = 'DSMT') DROP DATABASE DSMT
GO
CREATE DATABASE DSMT
GO
USE DSMT
GO

IF EXISTS (SELECT name FROM sys.tables WHERE name = 'ACCOUNTS') DROP TABLE ACCOUNTS
GO
CREATE TABLE ACCOUNTS (
    username varchar(30) primary key,
    password varchar(50) not null,
    email varchar(80),
    name nvarchar(60),
    flatform nvarchar(20) default 'SYSTEM',
    unique(email, flatform)
);

IF EXISTS (SELECT name FROM sys.tables WHERE name = 'ROLES') DROP TABLE ROLES
GO
CREATE TABLE ROLES (
    id varchar(10) primary key,
    name nvarchar(20)
);

IF EXISTS (SELECT name FROM sys.tables WHERE name = 'AUTHORITIES') DROP TABLE AUTHORITIES
GO
CREATE TABLE AUTHORITIES (
    account_id varchar(30) foreign key references ACCOUNTS(username)
        on update cascade on delete cascade not null,
    role_id varchar(10) foreign key references ROLES(id) not null,
    primary key (account_id, role_id)
);

IF EXISTS (SELECT name FROM sys.tables WHERE name = 'CATEGORIES') DROP TABLE CATEGORIES
GO
CREATE TABLE CATEGORIES (
    id char(8) primary key,
    name nvarchar(30) not null
);

IF EXISTS (SELECT name FROM sys.tables WHERE name = 'PRODUCTS') DROP TABLE PRODUCTS
GO
CREATE TABLE PRODUCTS (
    id int identity primary key,
    cosPrice float,
    proPrice float,
    name nvarchar(50),
    descript nvarchar(255),
    category_id char(8) foreign key references CATEGORIES(id) not null,
    account_id varchar(30) foreign key references ACCOUNTS(username)
    on update cascade on delete cascade not null,
);

IF EXISTS (SELECT name FROM sys.tables WHERE name = 'PRODUCT_IMAGES') DROP TABLE PRODUCT_IMAGES
GO
CREATE TABLE PRODUCT_IMAGES (
    product_id int foreign key references PRODUCTS(id)
    on update cascade on delete cascade not null,
	image nvarchar(255) not null,
	primary key(product_id, image)
);

IF EXISTS (SELECT name FROM sys.tables WHERE name = 'COMMENTS') DROP TABLE COMMENTS
GO
CREATE TABLE COMMENTS (
    account_id varchar(30) foreign key references ACCOUNTS(username)
    on update cascade on delete cascade not null,
    product_id int foreign key references PRODUCTS(id),
    regTime datetime default getDate(),
	descript nvarchar(255),
	evaluate int default 0,
    primary key(account_id, regTime),

);

IF EXISTS (SELECT name FROM sys.tables WHERE name = 'ORDERS') DROP TABLE ORDERS
GO
CREATE TABLE ORDERS (
    id int identity primary key,
	address nvarchar(80),
    regTime datetime default getDate(),
    account_id varchar(30) foreign key references ACCOUNTS(username)
    on update cascade on delete cascade not null,
);

IF EXISTS (SELECT name FROM sys.tables WHERE name = 'ORDER_DETAILS') DROP TABLE ORDER_DETAILS
GO
CREATE TABLE ORDER_DETAILS (
    order_id int foreign key references ORDERS(id) 
    on update cascade on delete cascade not null,
    product_id int foreign key references PRODUCTS(id),
	primary key(order_id, product_id),
    oldPrice float default 0,
    quantity int default 1
);

IF EXISTS (SELECT name FROM sys.tables WHERE name = 'ORDER_STATUS') DROP TABLE ORDER_STATUS
GO
CREATE TABLE ORDER_STATUS (
    order_id int foreign key references ORDERS(id)
    on update cascade on delete cascade not null,
    account_id varchar(30) foreign key references ACCOUNTS(username),
    status int default 0,
    descript nvarchar(150),
    primary key(order_id)
);

