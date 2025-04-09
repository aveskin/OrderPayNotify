--liquibase formatted sql

--changeset aveskin:create-outbox_events-table
--comment create table order_schema.orders
create table order_schema.outbox_events
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id   BIGINT      NOT NULL,
    payload    TEXT        NOT NULL,
    status     VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);
--rollback drop table order_schema.outbox_events;

