INSERT INTO scaffold_templates(name, description) VALUES ('invoice', 'Invoice Template');

INSERT INTO scaffold_template_properties(scaffold_template_id, name, type_id, default_value)
VALUES ((SELECT id FROM scaffold_templates WHERE name = 'invoice'), 'company_name', 1, 'enter_name_here');
INSERT INTO scaffold_template_properties(scaffold_template_id, name, type_id, default_value)
VALUES ((SELECT id FROM scaffold_templates WHERE name = 'invoice'), 'company_address', 1, 'enter_address_here');
INSERT INTO scaffold_template_properties(scaffold_template_id, name, type_id)
VALUES ((SELECT id FROM scaffold_templates WHERE name = 'invoice'), 'invoice_date', 1);
INSERT INTO scaffold_template_properties(scaffold_template_id, name, type_id)
VALUES ((SELECT id FROM scaffold_templates WHERE name = 'invoice'), 'invoice_number', 1);
INSERT INTO scaffold_template_properties(scaffold_template_id, name, type_id)
VALUES ((SELECT id FROM scaffold_templates WHERE name = 'invoice'), 'customer_name', 1);
INSERT INTO scaffold_template_properties(scaffold_template_id, name, type_id)
VALUES ((SELECT id FROM scaffold_templates WHERE name = 'invoice'), 'customer_address', 1);
INSERT INTO scaffold_template_properties(scaffold_template_id, name, type_id)
VALUES ((SELECT id FROM scaffold_templates WHERE name = 'invoice'), 'service_or_goods_type', 1);
INSERT INTO scaffold_template_properties(scaffold_template_id, name, type_id)
VALUES ((SELECT id FROM scaffold_templates WHERE name = 'invoice'), 'service_or_goods_amour', 1);
INSERT INTO scaffold_template_properties(scaffold_template_id, name, type_id)
VALUES ((SELECT id FROM scaffold_templates WHERE name = 'invoice'), 'vat_rate', 1);

INSERT INTO scaffold_templates(name, description) VALUES ('game-purchases', 'In-game Purchases Template');

INSERT INTO scaffold_template_properties(scaffold_template_id, name, type_id, default_value)
VALUES ((SELECT id FROM scaffold_templates WHERE name = 'game-purchases'), 'buy_currency', 1, '+5');
INSERT INTO scaffold_template_properties(scaffold_template_id, name, type_id)
VALUES ((SELECT id FROM scaffold_templates WHERE name = 'game-purchases'), 'user_id', 1);