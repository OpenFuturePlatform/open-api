CREATE TABLE currencies(
  id INT PRIMARY KEY,
  key VARCHAR NOT NULL UNIQUE
);

INSERT INTO currencies(id, key) VALUES (1, 'USD');
INSERT INTO currencies(id, key) VALUES (2, 'GBP');
INSERT INTO currencies(id, key) VALUES (3, 'EUR');
INSERT INTO currencies(id, key) VALUES (4, 'CNY');
INSERT INTO currencies(id, key) VALUES (5, 'ETH');

CREATE TABLE property_types(
  id INT PRIMARY KEY,
  key VARCHAR NOT NULL UNIQUE
);

INSERT INTO property_types(id, key) VALUES (1, 'STRING');
INSERT INTO property_types(id, key) VALUES (2, 'NUMBER');
INSERT INTO property_types(id, key) VALUES (3, 'BOOLEAN');

CREATE TABLE users(
  id BIGSERIAL PRIMARY KEY,
  google_id VARCHAR UNIQUE NOT NULL,
  credits INT NOT NULL
);

CREATE TABLE open_keys(
  id BIGSERIAL PRIMARY KEY,
  value VARCHAR UNIQUE NOT NULL,
  user_id BIGINT REFERENCES users,
  enabled BOOLEAN NOT NULL DEFAULT TRUE,
  expired_date TIMESTAMP
);

CREATE TABLE scaffolds(
  id BIGSERIAL PRIMARY KEY,
  address VARCHAR UNIQUE NOT NULL,
  open_key_id BIGINT NOT NULL REFERENCES open_keys,
  abi VARCHAR NOT NULL,
  developer_address VARCHAR NOT NULL,
  description VARCHAR NOT NULL,
  fiat_amount VARCHAR NOT NULL,
  currency_id INT NOT NULL REFERENCES currencies,
  conversion_amount VARCHAR NOT NULL,
  enabled BOOLEAN NOT NULL DEFAULT FALSE,
  web_hook VARCHAR
);

CREATE TABLE scaffold_properties(
  id BIGSERIAL PRIMARY KEY,
  scaffold_id BIGINT NOT NULL REFERENCES scaffolds,
  name VARCHAR NOT NULL,
  type_id INT NOT NULL REFERENCES property_types,
  default_value VARCHAR NOT NULL
);

CREATE TABLE transactions(
  id BIGSERIAL PRIMARY KEY,
  scaffold_id BIGINT NOT NULL REFERENCES scaffolds,
  data VARCHAR NOT NULL,
  type VARCHAR NOT NULL
)