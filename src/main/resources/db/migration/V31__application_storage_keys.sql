alter table user_applications drop column currency_id;

alter table user_applications
    add column if not exists api_access_key VARCHAR,
    add column if not exists api_secret_key VARCHAR;