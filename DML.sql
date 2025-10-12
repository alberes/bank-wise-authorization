SCRIPT TO 'backup.sql';

SELECT * FROM client;
SELECT * FROM client_account;
SELECT * FROM client_account_scope;
SELECT * FROM transaction_account;

SELECT * FROM client_account
JOIN client_account_scope ON client_account.id = client_account_scope.client_account_id

SELECT * FROM client_account
JOIN transaction_account ON client_account.id = transaction_account.client_account_id;

INSERT INTO "PUBLIC"."CLIENT" VALUES
(UUID '3de324ca-cc6c-4eee-9890-bd076f8ad098', 'admin-client-id', '$2a$12$40ulvRdrXkyEszgVHD/kEOdZTEzJpnrM7JN0WqGzoFkNJ3eSLRrm.', TIMESTAMP '2025-10-12 20:03:40.681888', TIMESTAMP '2025-10-12 20:03:40.681888', 'http://localhost:8080/login/oauth2/code/bank-wise-client-oidc', 'ADMIN'),
(UUID '2c7b4045-9ffd-4a5d-b2b2-e4cd1327ef9e', 'postman-client', '$2a$12$i.wdvKDXBlRimxwa0.CWVe7HBlQS/VAka9kYMEDBxIlFALln5STxS', TIMESTAMP '2025-10-12 20:04:48.500076', TIMESTAMP '2025-10-12 20:04:48.500076', 'http://localhost:9090/login/oauth2/code/bank-wise-client-oidc', 'USER');

INSERT INTO "PUBLIC"."CLIENT_ACCOUNT" VALUES
(UUID '76f04321-1a2d-4b6a-af85-8738a86b5666', TIMESTAMP '2025-10-12 20:04:53.065343', TIMESTAMP '2025-10-12 20:04:53.065343', '59964669020', 'postman@postman.com', 'Posterman User', '$2a$12$s0iKvfLdTJ6OX9l4tG7XsurbDKQdA2FNvYlOffmHGUacB/2n8Xqae');

INSERT INTO "PUBLIC"."CLIENT_ACCOUNT_SCOPE" VALUES
(UUID '76f04321-1a2d-4b6a-af85-8738a86b5666', 'READ'),
(UUID '76f04321-1a2d-4b6a-af85-8738a86b5666', 'DELETE'),
(UUID '76f04321-1a2d-4b6a-af85-8738a86b5666', 'UPDATE'),
(UUID '76f04321-1a2d-4b6a-af85-8738a86b5666', 'WRITE');
