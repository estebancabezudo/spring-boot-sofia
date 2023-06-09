DROP TABLE IF EXISTS sites;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS accounts_users;
DROP TABLE IF EXISTS emails;
DROP TABLE IF EXISTS authorities;

CREATE TABLE sites (
  id INT NOT NULL,
  name VARCHAR(60) NOT NULL
);

CREATE TABLE accounts (
  id INT NOT NULL,
  site_id INT NOT NULL
);

CREATE TABLE users (
  id INT NOT NULL,
  email_id INT NOT NULL,
  password VARCHAR(500) NOT NULL,
  enabled INT NOT NULL
);

CREATE TABLE accounts_users (
  account_id INT NOT NULL,
  user_id INT NOT NULL
);

CREATE TABLE emails (
  id INT NOT NULL,
  email VARCHAR(100) NOT NULL
);

CREATE TABLE authorities (
  user_id INT NOT NULL,
  authority VARCHAR(50) NOT NULL
);

INSERT INTO sites (id, name) VALUES (1, 'localhost');
INSERT INTO sites (id, name) VALUES (2, 'example.com');

INSERT INTO accounts (id, site_id) VALUES(1, 1);
INSERT INTO accounts (id, site_id) VALUES(2, 1);

INSERT INTO users (id, email_id, password, enabled) VALUES (1, 1, '{bcrypt}$2a$10$KalncANBJR3NRyYy/i/Cr.iebrNyXUvSSJ//6Wm.JsFJqKueNaIJa', 1);
INSERT INTO users (id, email_id, password, enabled) VALUES (2, 2, '{bcrypt}$2a$10$KalncANBJR3NRyYy/i/Cr.iebrNyXUvSSJ//6Wm.JsFJqKueNaIJa', 1);
INSERT INTO users (id, email_id, password, enabled) VALUES (3, 2, '{bcrypt}$2a$10$KalncANBJR3NRyYy/i/Cr.iebrNyXUvSSJ//6Wm.JsFJqKueNaIJa', 1);

INSERT INTO emails (id, email) VALUES (1, 'esteban@cabezudo.net');
INSERT INTO emails (id, email) VALUES (2, 'sofia@cabezudo.net');

INSERT INTO accounts_users (account_id, user_id) VALUES(1, 1);
INSERT INTO accounts_users (account_id, user_id) VALUES(1, 2);
INSERT INTO accounts_users (account_id, user_id) VALUES(2, 1);

INSERT INTO authorities (user_id, authority) VALUES (1, 'user');
INSERT INTO authorities (user_id, authority) VALUES (1, 'admin');
INSERT INTO authorities (user_id, authority) VALUES (2, 'user');
INSERT INTO authorities (user_id, authority) VALUES (3, 'user');
INSERT INTO authorities (user_id, authority) VALUES (3, 'admin');
