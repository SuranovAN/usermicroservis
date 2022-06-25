create schema if not exists public;

create table if not exists users
(
    id           serial primary key,
    name         varchar,
    surname      varchar,
    patronymic   varchar,
    creationdate varchar
);

insert into users values (1, 'testUser', 'testSurname', 'testPatronymic', 'testDate');