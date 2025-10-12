create table client (
    id uuid not null,
    client_id varchar(255) not null,
    client_secret varchar(255) not null,
    created_date timestamp(6) not null,
    last_modified_date timestamp(6) not null,
    redirect_uri varchar(255) not null,
    scope varchar(255) not null,
    primary key (id)
)

create table client_account (
    id uuid not null,
    created_date timestamp(6) not null,
    last_modified_date timestamp(6) not null,
    legal_entity_number varchar(11) not null,
    login varchar(100) not null,
    name varchar(100) not null,
    password varchar(200) not null,
    primary key (id)
)

create table transaction_account (
    id uuid not null,
    created_date timestamp(6) not null,
    transaction_type integer not null,
    transaction_value numeric(38,2) not null,
    client_account_id uuid,
    primary key (id)
)

create table user_account_scope (
    client_account_id uuid not null,
    scopes varchar(255)
)
alter table if exists client
   drop constraint if exists UKbfjdoy2dpussylq7g1s3s1tn8

alter table if exists client
   add constraint UKbfjdoy2dpussylq7g1s3s1tn8 unique (client_id)

alter table if exists client_account
   drop constraint if exists UKk3je210x66lmdxa1lt5vh9cyi

alter table if exists client_account
   add constraint UKk3je210x66lmdxa1lt5vh9cyi unique (legal_entity_number)

alter table if exists client_account
   drop constraint if exists UK1oj1ge9ebf1tqakkgq9esirqx

alter table if exists client_account
   add constraint UK1oj1ge9ebf1tqakkgq9esirqx unique (login)

alter table if exists transaction_account
   add constraint FKoks4ybf1h8vjwp88322yqy2qo
   foreign key (client_account_id)
   references client_account

alter table if exists user_account_scope
   add constraint FKjc7fbvi4c6cuow85tpslgjloh
   foreign key (client_account_id)
   references client_account