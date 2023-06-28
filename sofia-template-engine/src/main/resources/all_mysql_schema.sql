DROP DATABASE sofia;
CREATE DATABASE sofia;

USE `sofia`;
DROP TABLE IF EXISTS `accounts_users`;
DROP TABLE IF EXISTS `accounts`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `sites`;
DROP TABLE IF EXISTS `emails`;
DROP TABLE IF EXISTS `web_clients`;
DROP TABLE IF EXISTS `authorities`;
DROP TABLE IF EXISTS `administrative_divisions`;
DROP TABLE IF EXISTS `places`;
DROP TABLE IF EXISTS `user_account_preferences`;
DROP TABLE IF EXISTS `user_preferences`;
DROP TABLE IF EXISTS `administrative_division_types`;
DROP TABLE IF EXISTS `administrative_division_names`;
DROP TABLE IF EXISTS `sites_people`;
DROP TABLE IF EXISTS `people`;
DROP TABLE IF EXISTS `languages`;
DROP TABLE IF EXISTS `countries`;
DROP TABLE IF EXISTS `phone_formats`;
DROP TABLE IF EXISTS `phones`;
DROP TABLE IF EXISTS `person_phones`;
DROP TABLE IF EXISTS `people_users`;

CREATE TABLE `sites` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(60) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `emails` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `accounts` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `site_id` INT NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_accounts_site` FOREIGN KEY (`site_id`) REFERENCES `sofia`.`sites` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `users` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `email_id` INT(11) NOT NULL,
  `password` VARCHAR(100),
  `locale` VARCHAR(7) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_email` (`email_id`),
  CONSTRAINT `fk_users_email` FOREIGN KEY (`email_id`) REFERENCES `sofia`.`emails` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `accounts_users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `account_id` INT(11) NOT NULL,
  `user_id` INT(11) NOT NULL,
  `owner` BOOLEAN NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_account_user` (`account_id`, `user_id`),
  CONSTRAINT `fk_accounts_users_account` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_accounts_users_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user_account_preferences` (
  `account_user_id` INT(11) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `value` VARCHAR(50) NOT NULL,
  UNIQUE KEY `ix_name` (`name`),
  CONSTRAINT `fk_user_account_preferences_accounts_users` FOREIGN KEY (`account_user_id`) REFERENCES `accounts_users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user_preferences` (
  `user_id` INT(11) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `value` VARCHAR(50) NOT NULL,
  UNIQUE KEY `ix_user_name` (`user_id`, `name`),
  CONSTRAINT `fk_user_preferences_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `authorities` (
  `account_user_id` INT(11) NOT NULL,
  `authority` VARCHAR(50) NOT NULL,
  UNIQUE KEY `ix_auth_username` (`account_user_id`, `authority`),
  CONSTRAINT `fk_authorities_accounts_users` FOREIGN KEY (`account_user_id`) REFERENCES `accounts_users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `countries` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL,
  `code` CHAR(2) NOT NULL,
  `language_id` INT(11) NOT NULL,
  `phone_country_code` INT(3),
  `phone_format_id` INT(11),
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `places` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `account_id` INT NOT NULL,
  `name` VARCHAR(60) NOT NULL,
  `street` VARCHAR(120) NOT NULL,
  `number` VARCHAR(6) NOT NULL,
  `interior_number` VARCHAR(6) NOT NULL,
  `references` VARCHAR(250) NOT NULL,
  `postal_code` VARCHAR(10) NOT NULL,
  `country_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_places_account` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`),
  CONSTRAINT `fk_places_country` FOREIGN KEY (`country_id`) REFERENCES `countries` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `administrative_division_types` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `administrative_division_names` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `administrative_divisions` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `place_id` INT NOT NULL,
  `type_id` INT NOT NULL,
  `name_id` INT NOT NULL,
  `enabled` BOOLEAN DEFAULT TRUE,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_place_type` (`place_id`,`type_id`),
  CONSTRAINT `fk_administrative_divisions_place` FOREIGN KEY (`place_id`) REFERENCES `places` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_administrative_divisions_type` FOREIGN KEY (`type_id`) REFERENCES `administrative_division_types` (`id`),
  CONSTRAINT `fk_administrative_divisions_name` FOREIGN KEY (`name_id`) REFERENCES `administrative_division_names` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `people` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `second_name` VARCHAR(100) NOT NULL,
  `last_name` VARCHAR(100) NOT NULL,
  `second_last_name` VARCHAR(100) NOT NULL,
  `date_of_birth` DATE NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `users_people` (
  `user_id` INT(11) NOT NULL,
  `person_id` INT(11) NOT NULL,
  PRIMARY KEY (`user_id`, `person_id`),
  CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_person_id` FOREIGN KEY (`person_id`) REFERENCES `people` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `languages` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `code` CHAR(2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `phone_formats` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `format` VARCHAR(16) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_format` (`format`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `phones` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `number` VARCHAR(20) NOT NULL,
  `country_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `person_phones` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `person_id` INT(11) NOT NULL,
  `phone_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_person_phone` (`person_id`, `phone_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `people_users` (
  `person_id` INT(11) NOT NULL,
  `user_id` INT(11) NOT NULL,
  PRIMARY KEY (`person_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `sofia`.`sites` (name) VALUES ('localhost');
INSERT INTO `sofia`.`sites` (name) VALUES ('cabezudo.net');
INSERT INTO `sofia`.`sites` (name) VALUES ('otorrinos.condesa.info');
INSERT INTO `sofia`.`sites` (name) VALUES ('datosinutilesparaimpresionarenlasfiestas.com');

INSERT INTO `accounts` (site_id, name) VALUES(1, 'localhost');
INSERT INTO `accounts` (site_id, name) VALUES(1, 'sofia');
INSERT INTO `accounts` (site_id, name) VALUES(1, 'sofiaexample');
INSERT INTO `accounts` (site_id, name) VALUES(2, 'estebanexample');

INSERT INTO `sofia`.`emails` (email) VALUES ('esteban@cabezudo.net');
INSERT INTO `sofia`.`emails` (email) VALUES ('sofia@cabezudo.net');
INSERT INTO `sofia`.`emails` (email) VALUES ('sofia@example.com');
INSERT INTO `sofia`.`emails` (email) VALUES ('esteban@example.com');

INSERT INTO `users` (email_id, password, locale, enabled) VALUES (1, '{bcrypt}$2a$10$KalncANBJR3NRyYy/i/Cr.iebrNyXUvSSJ//6Wm.JsFJqKueNaIJa', 'es',1);
INSERT INTO `users` (email_id, password, locale, enabled) VALUES (2, '{bcrypt}$2a$10$KalncANBJR3NRyYy/i/Cr.iebrNyXUvSSJ//6Wm.JsFJqKueNaIJa', 'es',1);
INSERT INTO `users` (email_id, password, locale, enabled) VALUES (3, '{bcrypt}$2a$10$KalncANBJR3NRyYy/i/Cr.iebrNyXUvSSJ//6Wm.JsFJqKueNaIJa', 'es',1);
INSERT INTO `users` (email_id, password, locale, enabled) VALUES (4, '{bcrypt}$2a$10$KalncANBJR3NRyYy/i/Cr.iebrNyXUvSSJ//6Wm.JsFJqKueNaIJa', 'es',1);

INSERT INTO `accounts_users` (account_id, user_id, owner) VALUES (1, 1, true);
INSERT INTO `accounts_users` (account_id, user_id, owner) VALUES (2, 2, true);
INSERT INTO `accounts_users` (account_id, user_id, owner) VALUES (3, 3, true);
INSERT INTO `accounts_users` (account_id, user_id, owner) VALUES (4, 4, true);
INSERT INTO `accounts_users` (account_id, user_id, owner) VALUES (1, 2, false);
INSERT INTO `accounts_users` (account_id, user_id, owner) VALUES (3, 1, false);
INSERT INTO `accounts_users` (account_id, user_id, owner) VALUES (3, 2, false);
INSERT INTO `accounts_users` (account_id, user_id, owner) VALUES (3, 4, false);

INSERT INTO `authorities` (account_user_id, authority) VALUES (1, 'admin');
INSERT INTO `authorities` (account_user_id, authority) VALUES (1, 'user');
INSERT INTO `authorities` (account_user_id, authority) VALUES (4, 'admin');
INSERT INTO `authorities` (account_user_id, authority) VALUES (4, 'user');
INSERT INTO `authorities` (account_user_id, authority) VALUES (5, 'user');
INSERT INTO `authorities` (account_user_id, authority) VALUES (6, 'user');

INSERT INTO `administrative_division_types` (name) VALUES ('state');
INSERT INTO `administrative_division_types` (name) VALUES ('municipality');
INSERT INTO `administrative_division_types` (name) VALUES ('city');
INSERT INTO `administrative_division_types` (name) VALUES ('colony');
INSERT INTO `administrative_division_types` (name) VALUES ('delegation');

INSERT INTO `administrative_division_names` (name) VALUES ('MX-CMX');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-AGU');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-BCN');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-BCS');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-CAM');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-COA');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-COL');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-CHP');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-CHH');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-DUR'); # 10
INSERT INTO `administrative_division_names` (name) VALUES ('MX-GUA');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-GRO');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-HID');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-JAL');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-MEX');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-MIC');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-MOR');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-NAY');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-NLE');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-OAX'); # 20
INSERT INTO `administrative_division_names` (name) VALUES ('MX-PUE');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-QUE');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-ROO');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-SLP');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-SIN');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-SON');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-TAB');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-TAM');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-TLA');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-VER'); # 30
INSERT INTO `administrative_division_names` (name) VALUES ('MX-YUC');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-ZAC');
INSERT INTO `administrative_division_names` (name) VALUES ('Cozumel'); # 33
INSERT INTO `administrative_division_names` (name) VALUES ('Independencia');
INSERT INTO `administrative_division_names` (name) VALUES ('CDMX'); # 35
INSERT INTO `administrative_division_names` (name) VALUES ('Miguel Hidalgo');
INSERT INTO `administrative_division_names` (name) VALUES ('Popotla');
INSERT INTO `administrative_division_names` (name) VALUES ('Cumbres 1er Sector');
INSERT INTO `administrative_division_names` (name) VALUES ('Monterrey');

INSERT INTO `countries` (name, code, language_id, phone_country_code, phone_format_id) VALUES ('mexico', 'mx', 1, 52, 1);

INSERT INTO `places`
    (account_id, name, street, number, interior_number, `references`, postal_code, country_id)
    VALUES (1, 'Casa', 'Xel-Ha', '864', '', 'Entre 40 y 40 bis', '77664', 1);

INSERT INTO `administrative_divisions` (place_id, type_id, name_id) VALUES (1, 1, 23);
INSERT INTO `administrative_divisions` (place_id, type_id, name_id) VALUES (1, 2, 33);
INSERT INTO `administrative_divisions` (place_id, type_id, name_id) VALUES (1, 3, 33);
INSERT INTO `administrative_divisions` (place_id, type_id, name_id) VALUES (1, 4, 34);

INSERT INTO `places`
    (account_id, name, street, number, interior_number, `references`, postal_code, country_id)
    VALUES (1, 'Departamento', 'Felipe Carrillo puerto', '181', 'D504', '', '11400', 1);

INSERT INTO `administrative_divisions` (place_id, type_id, name_id) VALUES (2, 1, 1);
INSERT INTO `administrative_divisions` (place_id, type_id, name_id) VALUES (2, 3, 35);
INSERT INTO `administrative_divisions` (place_id, type_id, name_id) VALUES (2, 5, 36);
INSERT INTO `administrative_divisions` (place_id, type_id, name_id) VALUES (2, 4, 37);

INSERT INTO `places`
    (account_id, name, street, number, interior_number, `references`, postal_code, country_id)
    VALUES (2, 'Casa vieja', '11va Av', '113A', '', '', '64610', 1);

INSERT INTO `administrative_divisions` (place_id, type_id, name_id) VALUES (3, 4, 38);
INSERT INTO `administrative_divisions` (place_id, type_id, name_id) VALUES (3, 3, 39);
INSERT INTO `administrative_divisions` (place_id, type_id, name_id) VALUES (3, 1, 19);
INSERT INTO `administrative_divisions` (place_id, type_id, name_id) VALUES (3, 2, 39);

INSERT INTO `languages` (code) VALUES ('es');

INSERT INTO `phone_formats` (format) VALUES ('## #### ####');

INSERT INTO `phones` (number, country_id) VALUES (5541268778, 1);
INSERT INTO `phones` (number, country_id) VALUES (9873435456, 1);
INSERT INTO `phones` (number, country_id) VALUES (5510267856, 1);

INSERT INTO `person_phones` (person_id, phone_id) VALUES (1, 1);
INSERT INTO `person_phones` (person_id, phone_id) VALUES (1, 2);
INSERT INTO `person_phones` (person_id, phone_id) VALUES (2, 3);

INSERT INTO `people` (name, second_name, last_name, second_last_name, date_of_birth) VALUES ('Esteban', 'Ismael', 'Cabezudo', 'Trabanco', '1974-1-30');
INSERT INTO `people` (name, second_name, last_name, second_last_name, date_of_birth) VALUES ('Elba', 'Alicia', 'Araujo', 'Cieza', '1997-3-31');
INSERT INTO `people` (name, second_name, last_name, second_last_name, date_of_birth) VALUES ('Yadira', 'Vanessa', 'Bustos', 'Silca', '1985-5-6');
INSERT INTO `people` (name, second_name, last_name, second_last_name, date_of_birth) VALUES ('Isidro', 'Juan Pablo', 'Maradona', 'Crispín', '1996-7-18');
INSERT INTO `people` (name, second_name, last_name, second_last_name, date_of_birth) VALUES ('Covadonga', 'Remigio', 'Garza', 'Rodea', '1989-10-27');
INSERT INTO `people` (name, second_name, last_name, second_last_name, date_of_birth) VALUES ('Telesforo', 'Mauricio', 'Macías', 'Zuluaga', '1983-3-6');
INSERT INTO `people` (name, second_name, last_name, second_last_name, date_of_birth) VALUES ('Begoña', 'Noelia', 'Suero', 'Guerrero', '1986-12-22');
INSERT INTO `people` (name, second_name, last_name, second_last_name, date_of_birth) VALUES ('Cayetano', 'Moisés', 'De la Fuente', 'Espinoza', '1991-6-23');
INSERT INTO `people` (name, second_name, last_name, second_last_name, date_of_birth) VALUES ('Esmeralda', 'Rosaura', 'Fernández', 'Tancarin', '1999-10-17');
INSERT INTO `people` (name, second_name, last_name, second_last_name, date_of_birth) VALUES ('Héctor', 'Bonifacio', 'Ibáñez', 'Cruz', '1992-6-30');
INSERT INTO `people` (name, second_name, last_name, second_last_name, date_of_birth) VALUES ('Herminio', 'Amarant0', 'Jiménez', 'Aquino', '1999-9-24');
INSERT INTO `people` (name, second_name, last_name, second_last_name, date_of_birth) VALUES ('Paco', 'Sandr0', 'García', 'Gómez', '1990-12-4');
INSERT INTO `people` (name, second_name, last_name, second_last_name, date_of_birth) VALUES ('Clara', 'Melania', 'Amador', 'Valencia', '1981-3-23');
INSERT INTO `people` (name, second_name, last_name, second_last_name, date_of_birth) VALUES ('Bernardita', 'Ximena', 'Gallardo', 'Cordoba', '1992-9-24');
INSERT INTO `people` (name, second_name, last_name, second_last_name, date_of_birth) VALUES ('Haroldo', 'Atanasio', 'Bolívar', 'Alvarez', '1991-2-23');
INSERT INTO `people` (name, second_name, last_name, second_last_name, date_of_birth) VALUES ('Demetrio', 'Nataniel', 'Silva', 'Fuentes', '1989-2-13');
INSERT INTO `people` (name, second_name, last_name, second_last_name, date_of_birth) VALUES ('Nerea', 'Yaiza', 'Abascal', 'Vera', '1988-12-17');
INSERT INTO `people` (name, second_name, last_name, second_last_name, date_of_birth) VALUES ('Laurentino', 'Antonio', 'Arias', 'Calderon', '1988-1-18');
INSERT INTO `people` (name, second_name, last_name, second_last_name, date_of_birth) VALUES ('María', 'Mercedes', 'Ybarra', 'LAgos', '1982-7-25');
INSERT INTO `people` (name, second_name, last_name, second_last_name, date_of_birth) VALUES ('Elisabet', 'Noelia', 'Otero', 'Martinez', '1992-6-21');
INSERT INTO `people` (name, second_name, last_name, second_last_name, date_of_birth) VALUES ('Esther', 'Paloma', 'Amador', 'Mercado', '1994-5-22');

INSERT INTO `people_users` (person_id, user_id) VALUES (1, 1);
