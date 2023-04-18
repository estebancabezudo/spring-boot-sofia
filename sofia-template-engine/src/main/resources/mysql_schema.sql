USE `sofia`;
DROP TABLE IF EXISTS `authorities`;
DROP TABLE IF EXISTS `emails`;
DROP TABLE IF EXISTS `accounts_users`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `accounts`;
DROP TABLE IF EXISTS `sites`;

CREATE TABLE `sites` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(60) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `accounts` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `site_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_site` FOREIGN KEY (`site_id`) REFERENCES `sites` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `users` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `email_id` INT(11) NOT NULL,
  `password` VARCHAR(500) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_site_id_email` (`site_id`,`email_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `accounts_users` (
  `account_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`account_id`, `user_id`),
  CONSTRAINT `fk_accounts` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`),
  CONSTRAINT `fk_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `emails` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `authorities` (
  `user_id` INT(11) NOT NULL,
  `authority` VARCHAR(50) NOT NULL,
  UNIQUE KEY `ix_auth_username` (`user_id`,`authority`),
  CONSTRAINT `fk_authorities_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `sites` (name) VALUES ('localhost');
INSERT INTO `sites` (name) VALUES ('example.com');

INSERT INTO `accounts` (site_id) VALUES(1);
INSERT INTO `accounts` (site_id) VALUES(1);

INSERT INTO `users` (email_id, password, enabled) VALUES (1, 1, '{bcrypt}$2a$10$KalncANBJR3NRyYy/i/Cr.iebrNyXUvSSJ//6Wm.JsFJqKueNaIJa', 1);
INSERT INTO `users` (email_id, password, enabled) VALUES (1, 2, '{bcrypt}$2a$10$KalncANBJR3NRyYy/i/Cr.iebrNyXUvSSJ//6Wm.JsFJqKueNaIJa', 1);
INSERT INTO `users` (email_id, password, enabled) VALUES (2, 2, '{bcrypt}$2a$10$KalncANBJR3NRyYy/i/Cr.iebrNyXUvSSJ//6Wm.JsFJqKueNaIJa', 1);

INSERT INTO `emails` (email) VALUES ('esteban@cabezudo.net');
INSERT INTO `emails` (email) VALUES ('sofia@cabezudo.net');

INSERT INTO `accounts_users` (account_id, user_id) VALUES(1, 1);
INSERT INTO `accounts_users` (account_id, user_id) VALUES(1, 2);
INSERT INTO `accounts_users` (account_id, user_id) VALUES(2, 1);

INSERT INTO `authorities` (user_id, authority) VALUES (1, 'user');
INSERT INTO `authorities` (user_id, authority) VALUES (1, 'admin');
INSERT INTO `authorities` (user_id, authority) VALUES (2, 'user');
INSERT INTO `authorities` (user_id, authority) VALUES (3, 'user');
INSERT INTO `authorities` (user_id, authority) VALUES (3, 'admin');

SELECT * FROM `users`; SELECT * FROM emails; SELECT * FROM authorities;

SELECT * FROM users AS u, emails AS e, authorities AS a WHERE u.email_id = e.id AND u.id = a.user_id;

SELECT u.id AS id, s.name AS site_name, email AS username, password, enabled, authority FROM sites AS s LEFT JOIN users AS u ON s.id = u.site_id, emails AS e, authorities AS a WHERE u.email_id = e.id AND u.id = a.user_id AND site_id = 1 AND e.email = 'esteban@cabezudo.net';

SELECT u.id AS id, site_id, email AS username, password, enabled, authority FROM users AS u, emails AS e, authorities AS a WHERE u.email_id = e.id AND u.id = a.user_id AND site_id = ? AND e.email = ?;
