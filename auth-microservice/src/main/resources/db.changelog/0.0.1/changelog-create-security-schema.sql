--liquibase formatted sql

--changeset aveskin:create-security-schema
create schema security_schema;
--rollback drop schema security_schema;