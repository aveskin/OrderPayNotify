--liquibase formatted sql

--changeset aveskin:create-users-table
--comment create table security_schema.users
create table security_schema.users
(
    id         SERIAL PRIMARY KEY,
    username   VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE
);
--rollback drop table security_schema.users;

