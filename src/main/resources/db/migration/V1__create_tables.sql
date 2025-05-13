create table category(
    id serial primary key
    ,name varchar(50)
);

create table account(
    id serial primary key
    ,name varchar(50)
);

create type transaction_type as enum (
    'income'
    ,'expense'
);

create table transaction(
    id serial primary key
    ,amount decimal(19,4)
    ,name varchar(50)
    ,description text
    ,type transaction_type
    ,date date
    ,installment integer
);

create table balance(
    id serial primary key
    ,current_balance decimal(19,4)
    ,date date
);
