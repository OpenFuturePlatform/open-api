CREATE TABLE scaffold_templates(
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR UNIQUE NOT NULL,
  user_id BIGINT NOT NULL REFERENCES users,
  developer_address VARCHAR,
  description VARCHAR,
  fiat_amount VARCHAR,
  currency_id INT REFERENCES currencies,
  conversion_amount VARCHAR,
  web_hook VARCHAR
);

CREATE TABLE scaffold_template_properties(
  id BIGSERIAL PRIMARY KEY,
  scaffold_template_id BIGINT NOT NULL REFERENCES scaffold_templates,
  name VARCHAR,
  type_id INT REFERENCES property_types,
  default_value VARCHAR
);