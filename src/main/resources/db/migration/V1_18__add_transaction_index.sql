ALTER TABLE transactions ADD COLUMN index VARCHAR NOT NULL DEFAULT '0x1';
ALTER TABLE transactions DROP CONSTRAINT transactions_hash_key;
ALTER TABLE transactions ADD UNIQUE (hash, index);