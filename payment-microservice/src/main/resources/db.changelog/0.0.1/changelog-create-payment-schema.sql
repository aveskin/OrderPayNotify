--liquibase formatted sql

--changeset aveskin:create-payment-schema
create schema payment_schema;
--rollback drop schema payment_schema;