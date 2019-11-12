ALTER TABLE scaffolds
    ADD network VARCHAR(50);

UPDATE scaffolds
SET network = 'ethereum';

ALTER TABLE scaffolds
    ALTER COLUMN network SET NOT NULL;

CREATE INDEX idx_scaffolds_network
    ON scaffolds (network);

ALTER TABLE scaffold_properties
    RENAME TO ethereum_scaffold_properties;
ALTER TABLE scaffold_summaries
    RENAME TO ethereum_scaffold_summaries;
ALTER TABLE scaffold_templates
    RENAME TO ethereum_scaffold_templates;
ALTER TABLE share_holders
    RENAME TO ethereum_share_holders;
ALTER TABLE scaffold_template_properties
    RENAME TO ethereum_scaffold_template_properties;
ALTER TABLE transactions
    RENAME TO ethereum_transactions;

ALTER TABLE scaffolds
    ALTER address DROP NOT NULL,
    ALTER abi DROP NOT NULL,
    ALTER fiat_amount DROP NOT NULL,
    ALTER currency_id DROP NOT NULL,
    ALTER conversion_amount DROP NOT NULL,
    ALTER version_id DROP NOT NULL,
    ALTER version_id DROP DEFAULT;