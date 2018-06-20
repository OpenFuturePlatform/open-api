CREATE TABLE scaffold_summaries(
  id BIGSERIAL PRIMARY KEY,
  scaffold_id BIGINT NOT NULL REFERENCES scaffolds,
  transaction_index NUMERIC NOT NULL,
  token_balance NUMERIC NOT NULL,
  enabled BOOLEAN NOT NULL,
  date TIMESTAMP NOT NULL
);

-- ALTER TABLE scaffolds DROP COLUMN enabled;
ALTER TABLE scaffolds RENAME COLUMN developer_address TO vendor_address;