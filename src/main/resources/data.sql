/*INSERT INTO users (username, password, email) VALUES
                                                  ('Paulina', 'mylove', 'paulina@example.com'),
                                                  ('Jane_Doe', 'password', 'jane.doe@example.com');

INSERT INTO categories (name, description) VALUES
                                               ('Groceries', 'Expenses for groceries'),
                                               ('Bills', 'Monthly utility bills');

INSERT INTO accounts (user_id, account_number, balance) VALUES
                                                            ((SELECT user_id FROM users WHERE username = 'Paulina'), '1234567890', 1000.00),
                                                            ((SELECT user_id FROM users WHERE username = 'Jane_Doe'), '0987654321', 2000.00);

INSERT INTO transactions (account_id, date, amount, transaction_type, description, category_id) VALUES
                                                                                                    ((SELECT account_id FROM accounts WHERE account_number = '1234567890'), CURDATE(), 50.00, 'debit', 'Depilacija', (SELECT category_id FROM categories WHERE name = 'Groceries')),
                                                                                                    ((SELECT account_id FROM accounts WHERE account_number = '0987654321'), CURDATE(), 200.00, 'debit', 'Electricity bill', (SELECT category_id FROM categories WHERE name = 'Bills'));
*/