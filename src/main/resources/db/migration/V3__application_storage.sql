CREATE TABLE user_applications(
           id BIGSERIAL PRIMARY KEY,
           name VARCHAR UNIQUE NOT NULL,
           user_id BIGINT NOT NULL REFERENCES users,
           currency_id INT REFERENCES currencies,
           expiration_period INT,
           web_hook VARCHAR,
           active BOOLEAN  DEFAULT TRUE
);