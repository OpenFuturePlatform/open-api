CREATE TABLE IF NOT EXISTS user_custom_token(
          id BIGSERIAL PRIMARY KEY,
          name VARCHAR UNIQUE NOT NULL,
          symbol VARCHAR,
          decimal INT,
          address VARCHAR,
          user_id BIGINT REFERENCES users,
          active BOOLEAN  DEFAULT TRUE,
          token_type INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS user_address(
        id BIGSERIAL PRIMARY KEY,
        blockchain VARCHAR,
        address VARCHAR,
        webhook VARCHAR,
        user_id BIGINT REFERENCES users,
        active BOOLEAN  DEFAULT TRUE
);

INSERT INTO user_custom_token(name, symbol, decimal, address, token_type)
VALUES ('Tether USDT','USDT',6,'0xdac17f958d2ee523a2206206994597c13d831ec7', 0),
       ('USD Coin','USDC',6,'0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48', 0),
       ('Wrapped LUNA Token','LUNA',18,'0xd2877702675e6cEb975b4A1dFf9fb7BAF4C91ea9', 0),
       ('Binance USD','BUSD',18,'0x4Fabb145d64652a948d72533023f6E7A623C7C53',0);