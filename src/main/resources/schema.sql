/*
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL
);

CREATE TABLE categories (
                            category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            description TEXT
);

CREATE TABLE accounts (
                          account_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          user_id BIGINT NOT NULL,
                          account_number VARCHAR(255) NOT NULL,
                          balance DECIMAL(15, 2) NOT NULL,
                          FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE transactions (
                              transaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              account_id BIGINT NOT NULL,
                              date DATE NOT NULL,
                              amount DECIMAL(15, 2) NOT NULL,
                              transaction_type VARCHAR(50) NOT NULL,
                              description TEXT,
                              category_id BIGINT,
                              FOREIGN KEY (account_id) REFERENCES accounts(account_id),
                              FOREIGN KEY (category_id) REFERENCES categories(category_id)
);
*/