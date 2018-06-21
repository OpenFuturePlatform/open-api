CREATE TABLE scaffold_summaries(
  id BIGSERIAL PRIMARY KEY,
  scaffold_id BIGINT NOT NULL UNIQUE REFERENCES scaffolds,
  transaction_index NUMERIC NOT NULL,
  token_balance NUMERIC NOT NULL,
  enabled BOOLEAN NOT NULL,
  date TIMESTAMP NOT NULL
);

CREATE TABLE share_holders(
  id BIGSERIAL PRIMARY KEY,
  summary_id BIGINT NOT NULL REFERENCES scaffold_summaries,
  address VARCHAR NOT NULL,
  percent INT NOT NULL
);

ALTER TABLE scaffolds DROP COLUMN enabled;
ALTER TABLE scaffolds RENAME COLUMN developer_address TO vendor_address;