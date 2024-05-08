USE `sofia`;

DROP TABLE IF EXISTS `administrative_divisions`;
DROP TABLE IF EXISTS `administrative_division_types`;
DROP TABLE IF EXISTS `administrative_division_names`;
DROP TABLE IF EXISTS `places`;
DROP TABLE IF EXISTS `streets`;

CREATE TABLE `streets` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(80) NOT NULL,
  `verified` BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `places` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `account_id` INT NOT NULL,
  `name` VARCHAR(60) NOT NULL,
  `street_id` INT,
  `number` VARCHAR(6) NOT NULL,
  `interior_number` VARCHAR(20) NOT NULL,
  `corner_street_id` INT,
  `first_street_id` INT,
  `second_street_id` INT,
  `references` VARCHAR(250) NOT NULL,
  `postal_code` VARCHAR(10) NOT NULL,
  `country_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_account` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`),
  CONSTRAINT `fk_second_street` FOREIGN KEY (`second_street_id`) REFERENCES `streets` (`id`),
  CONSTRAINT `fk_country` FOREIGN KEY (`country_id`) REFERENCES `countries` (`id`)
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
  CONSTRAINT `fk_place` FOREIGN KEY (`place_id`) REFERENCES `places` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_type` FOREIGN KEY (`type_id`) REFERENCES `administrative_division_types` (`id`),
  CONSTRAINT `fk_name` FOREIGN KEY (`name_id`) REFERENCES `administrative_division_names` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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
INSERT INTO `administrative_division_names` (name) VALUES ('MX-DUR');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-GUA');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-GRO');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-HID');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-JAL');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-MEX');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-MIC');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-MOR');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-NAY');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-NLE');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-OAX');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-PUE');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-QUE');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-ROO');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-SLP');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-SIN');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-SON');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-TAB');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-TAM');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-TLA');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-VER');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-YUC');
INSERT INTO `administrative_division_names` (name) VALUES ('MX-ZAC');
