USE `sofia`;

DROP TABLE IF EXISTS `administrative_divisions`;
DROP TABLE IF EXISTS `administrative_division_types`;
DROP TABLE IF EXISTS `administrative_division_names`;
DROP TABLE IF EXISTS `places`;

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
  CONSTRAINT `fk_account` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`),
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

INSERT INTO `administrative_division_names` (name) VALUES ('Cozumel');
INSERT INTO `administrative_division_names` (name) VALUES ('Independencia');

INSERT INTO `administrative_division_names` (name) VALUES ('CDMX');
INSERT INTO `administrative_division_names` (name) VALUES ('Miguel Hidalgo');
INSERT INTO `administrative_division_names` (name) VALUES ('Popotla');


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

SELECT * FROM places AS p LEFT JOIN countries AS c ON p.country_id = c.id;

SELECT * FROM administrative_divisions AS a LEFT JOIN administrative_division_types as t ON a.type_id=t.id LEFT JOIN administrative_division_names as n ON a.name_id=n.id WHERE place_id=2;

SELECT a.id AS id, place_id, t.id AS type_id, t.name AS type_name, n.name AS name, enabled FROM administrative_divisions AS a LEFT JOIN administrative_division_types as t ON a.type_id=t.id LEFT JOIN administrative_division_names as n ON a.name_id=n.id WHERE place_id=2;
