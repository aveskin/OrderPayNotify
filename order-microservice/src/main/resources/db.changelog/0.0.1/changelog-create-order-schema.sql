--liquibase formatted sql

--changeset aveskin:create-order-schema
create schema order_schema;
--rollback drop schema order_schema;