--liquibase formatted sql

--changeset aveskin:create-payments-table
--comment create table payment_schema.payments
create table payment_schema.payments
(
    id         SERIAL PRIMARY KEY,
    status     VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
--rollback drop table payment_schema.payments;

