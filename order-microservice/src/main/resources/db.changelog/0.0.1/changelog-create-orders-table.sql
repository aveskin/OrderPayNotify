--liquibase formatted sql

--changeset aveskin:create-orders-table
--comment create table order_schema.orders
create table order_schema.orders
(
    id SERIAL PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    status VARCHAR(50),
    amount DECIMAL(19, 4),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
--rollback drop table order_schema.orders;

