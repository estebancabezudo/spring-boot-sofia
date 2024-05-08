DROP DATABASE sofia;
CREATE DATABASE sofia;

USE `sofia`;
DROP TABLE IF EXISTS `accounts_users`;
DROP TABLE IF EXISTS `web_client_data`;
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
DROP TABLE IF EXISTS `users_people`;
DROP TABLE IF EXISTS `names`;
DROP TABLE IF EXISTS `last_names`;

CREATE TABLE `sites` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(60) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `emails` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `address` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_address` (`address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `accounts` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `site_id` INT NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_site_name` (`site_id`, `name`),
  CONSTRAINT `fk_accounts_site` FOREIGN KEY (`site_id`) REFERENCES `sofia`.`sites` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `web_client_data` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `language` VARCHAR(2),
  `account_id` INT,
  `last_update` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_web_client_data_account` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email_id` INT NOT NULL,
  `password` VARCHAR(100),
  `locale` VARCHAR(7) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_email` (`email_id`),
  CONSTRAINT `fk_users_email` FOREIGN KEY (`email_id`) REFERENCES `sofia`.`emails` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `accounts_users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `account_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `owner` BOOLEAN NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_account_user` (`account_id`, `user_id`),
  CONSTRAINT `fk_accounts_users_account` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_accounts_users_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user_account_preferences` (
  `account_user_id` INT NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `value` VARCHAR(50) NOT NULL,
  UNIQUE KEY `ix_name` (`name`),
  CONSTRAINT `fk_user_account_preferences_accounts_users` FOREIGN KEY (`account_user_id`) REFERENCES `accounts_users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user_preferences` (
  `user_id` INT NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `value` VARCHAR(50) NOT NULL,
  UNIQUE KEY `ix_user_name` (`user_id`, `name`),
  CONSTRAINT `fk_user_preferences_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `authorities` (
  `account_user_id` INT NOT NULL,
  `authority` VARCHAR(50) NOT NULL,
  UNIQUE KEY `ix_auth_username` (`account_user_id`, `authority`),
  CONSTRAINT `fk_authorities_accounts_users` FOREIGN KEY (`account_user_id`) REFERENCES `accounts_users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `countries` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL,
  `code` CHAR(2) NOT NULL,
  `language_id` INT NOT NULL,
  `phone_country_code` INT(3),
  `phone_format_id` INT,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `streets` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(80) NOT NULL,
  `verified` BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `postal_codes` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(16) NOT NULL,
  `verified` BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_name` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `places` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `account_id` INT NOT NULL,
  `name` VARCHAR(60) NOT NULL,
  `street_id` INT,
  `number` VARCHAR(6),
  `interior_number` VARCHAR(20),
  `corner_street_id` INT,
  `first_street_id` INT,
  `second_street_id` INT,
  `references` VARCHAR(250),
  `postal_code_id` INT,
  `country_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_account` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`),
  CONSTRAINT `fk_street` FOREIGN KEY (`street_id`) REFERENCES `streets` (`id`),
  CONSTRAINT `fk_first_street` FOREIGN KEY (`first_street_id`) REFERENCES `streets` (`id`),
  CONSTRAINT `fk_second_street` FOREIGN KEY (`second_street_id`) REFERENCES `streets` (`id`),
  CONSTRAINT `fk_postal_code` FOREIGN KEY (`postal_code_id`) REFERENCES `postal_codes` (`id`),
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

CREATE TABLE `people` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `account_id` INT NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `second_name` VARCHAR(100) DEFAULT NULL,
  `last_name` VARCHAR(100) NOT NULL,
  `second_last_name` VARCHAR(100) DEFAULT NULL,
  `date_of_birth` DATE DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `users_people` (
  `user_id` INT NOT NULL,
  `person_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `person_id`),
  CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_person_id` FOREIGN KEY (`person_id`) REFERENCES `people` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `languages` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `code` CHAR(2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `phone_formats` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `format` VARCHAR(16) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_format` (`format`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `phones` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `country_code` INT(5) NOT NULL,
  `number` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_country_code_number` (`country_code`, `number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `person_phones` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `person_id` INT NOT NULL,
  `phone_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_person_phone` (`person_id`, `phone_id`),
  CONSTRAINT `fk_person` FOREIGN KEY (`person_id`) REFERENCES `people` (`id`),
  CONSTRAINT `fk_phone` FOREIGN KEY (`phone_id`) REFERENCES `phones` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `names` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(60) NOT NULL,
  `verified` BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_name_verified` (`name`, `verified`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `last_names` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `last_name` VARCHAR(60) NOT NULL,
  `verified` BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_last_name_verified` (`last_name`, `verified`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `primary_works` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(60) NOT NULL,
  `verified` BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_name_verified` (`name`, `verified`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `doctors` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `information_quality` INT DEFAULT 0,
  `stars` INT DEFAULT 0,
  `number_of_reviews` INT DEFAULT 0,
  `image_id` INT,
  `sex` CHAR(1),
  `title` VARCHAR(10),
  `name_id` INT NOT NULL,
  `second_name_id` INT,
  `last_name_id` INT,
  `mother_last_name_id` INT,
  `personal_phone_id` INT,
  `email_id` INT,
  `about` VARCHAR(300),
  `doctoralia_url` VARCHAR(200),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_doctors_name` FOREIGN KEY (`name_id`) REFERENCES `names` (`id`),
  CONSTRAINT `fk_doctors_second_name` FOREIGN KEY (`second_name_id`) REFERENCES `names` (`id`),
  CONSTRAINT `fk_doctors_last_name` FOREIGN KEY (`last_name_id`) REFERENCES `last_names` (`id`),
  CONSTRAINT `fk_doctors_mother_last_name` FOREIGN KEY (`mother_last_name_id`) REFERENCES `last_names` (`id`),
  CONSTRAINT `fk_doctors_personal_phone` FOREIGN KEY (`personal_phone_id`) REFERENCES `phones` (`id`),
  CONSTRAINT `fk_doctors_email` FOREIGN KEY (`email_id`) REFERENCES `emails` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `images` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `account_id` INT NOT NULL,
  `path` VARCHAR(200),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_images_account` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `medical_consultations` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `doctor_id` INT NOT NULL,
  `name` VARCHAR(60) NOT NULL,
  `place_id` INT,
  `verified` BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_name` (`doctor_id`, `name`, `place_id`),
  CONSTRAINT `fk_medical_consultations_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `fk_medical_consultations_place` FOREIGN KEY (`place_id`) REFERENCES `places` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `target_names` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) NOT NULL,
  `verified` BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_name_verified` (`name`, `verified`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `medical_consultations_targets` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `medical_consultation_id` INT NOT NULL,
  `target_name_id` INT NOT NULL,
  `age` INT(3) NOT NULL,
  `modifiers` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_medical_consultation_target` (`medical_consultation_id`, `target_name_id`),
  CONSTRAINT `fk_medical_consultation` FOREIGN KEY (`medical_consultation_id`) REFERENCES `medical_consultations` (`id`),
  CONSTRAINT `fk_target` FOREIGN KEY (`target_name_id`) REFERENCES `target_names` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `payment_methods` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) NOT NULL,
  `verified` BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_name_verified` (`name`, `verified`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `medical_consultations_payment_methods` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `medical_consultation_id` INT NOT NULL,
  `payment_method_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_medical_consultation_payment_method` (`medical_consultation_id`, `payment_method_id`),
  CONSTRAINT `fk_medical_consultations_payment_method_medical_consultation` FOREIGN KEY (`medical_consultation_id`) REFERENCES `medical_consultations` (`id`),
  CONSTRAINT `fk_medical_consultations_payment_method_payment_method` FOREIGN KEY (`payment_method_id`) REFERENCES `payment_methods` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `insurance_companies` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `verified` BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `medical_consultations_insurance_companies` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `medical_consultation_id` INT NOT NULL,
  `insurance_company_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_medical_consultation_insurance_company` (`medical_consultation_id`, `insurance_company_id`),
  CONSTRAINT `fk_medical_consultations_insurance_company_medical_consultation` FOREIGN KEY (`medical_consultation_id`) REFERENCES `medical_consultations` (`id`),
  CONSTRAINT `fk_medical_consultations_insurance_company_insurance_company` FOREIGN KEY (`insurance_company_id`) REFERENCES `insurance_companies` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `service_names` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `verified` BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_name_verified` (`name`, `verified`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `services` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `service_name_id` INT NOT NULL,
  `modifiers` VARCHAR(5),
  `description` VARCHAR(2000),
  `currency` VARCHAR(3),
  `cost` DECIMAL(10,2),
  `days` VARCHAR(7),
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_name_id` (`service_name_id`),
  CONSTRAINT `fk_service` FOREIGN KEY (`service_name_id`) REFERENCES `service_names` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `medical_consultations_services` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `medical_consultation_id` INT NOT NULL,
  `service_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_medical_consultation_service` (`medical_consultation_id`, `service_id`),
  CONSTRAINT `fk_medical_consultation_services` FOREIGN KEY (`medical_consultation_id`) REFERENCES `medical_consultations` (`id`),
  CONSTRAINT `fk_medical_consultations_services_service` FOREIGN KEY (`service_id`) REFERENCES `services` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `medical_consultations_phones` (
  `medical_consultation_id` INT NOT NULL,
  `phone_id` INT NOT NULL,
  PRIMARY KEY (`medical_consultation_id`, `phone_id`),
  CONSTRAINT `fk_medical_consultation_phone` FOREIGN KEY (`medical_consultation_id`) REFERENCES `medical_consultations` (`id`),
  CONSTRAINT `fk_medical_consultations_phone_phones` FOREIGN KEY (`phone_id`) REFERENCES `phones` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `conditions_treated` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `verified` BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_name_verified` (`name`, `verified`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `accolades` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `doctor_id` INT NOT NULL,
  `description` VARCHAR(1500) NOT NULL,
  `year` int(4),
  `verified` BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `doctor_primary_works` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `doctor_id` INT NOT NULL,
  `primary_work_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_doctor_primary_work` (`doctor_id`, `primary_work_id`),
  CONSTRAINT `fk_doctor_primary_works_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `fk_primary_work` FOREIGN KEY (`primary_work_id`) REFERENCES `primary_works` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `specialities` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(60) NOT NULL,
  `verified` BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_name_verified` (`name`, `verified`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `doctor_specialities` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `doctor_id` INT NOT NULL,
  `speciality_id` INT NOT NULL,
  `verified` BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_doctor_speciality` (`doctor_id`, `speciality_id`),
  CONSTRAINT `fk_specialities_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `fk_speciality` FOREIGN KEY (`speciality_id`) REFERENCES `specialities` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `doctors_conditions_treated` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `doctor_id` INT NOT NULL,
  `condition_treated_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_doctor_condition_treated` (`doctor_id`, `condition_treated_id`),
  CONSTRAINT `fk_doctors_conditions_treated_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `fk_condition_treated` FOREIGN KEY (`condition_treated_id`) REFERENCES `conditions_treated` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `educations` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `doctor_id` INT NOT NULL,
  `description` VARCHAR(1500) NOT NULL,
  `year` INT(4),
  PRIMARY KEY (`id`),
  `verified` BOOLEAN NOT NULL DEFAULT FALSE,
  CONSTRAINT `fk_doctors_educations_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `doctors_languages` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `doctor_id` INT NOT NULL,
  `language_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_doctor_language` (`doctor_id`, `language_id`),
  CONSTRAINT `fk_doctors_language_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `fk_language` FOREIGN KEY (`language_id`) REFERENCES `languages` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `licenses` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `doctor_id` INT NOT NULL,
  `license` VARCHAR(20) NOT NULL,
  `verified` BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_doctor_license` (`doctor_id`, `license`),
  CONSTRAINT `fk_licenses_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `doctoralia` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `doctor_id` INT NOT NULL,
  `url` VARCHAR(300) NOT NULL,
  `verified` BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ix_doctor` (`doctor_id`),
  CONSTRAINT `fk_doctor_doctoralia` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;































INSERT INTO `sofia`.`sites` (name) VALUES ('localhost');
INSERT INTO `sofia`.`sites` (name) VALUES ('cabezudo.net');
INSERT INTO `sofia`.`sites` (name) VALUES ('otorrinos.condesa.info');
INSERT INTO `sofia`.`sites` (name) VALUES ('datosinutilesparaimpresionarenlasfiestas.com');
INSERT INTO `sofia`.`sites` (name) VALUES ('medicina.digital');

INSERT INTO `accounts` (site_id, name) VALUES(1, 'localhost');
INSERT INTO `accounts` (site_id, name) VALUES(1, 'sofia');
INSERT INTO `accounts` (site_id, name) VALUES(1, 'sofiaexample');
INSERT INTO `accounts` (site_id, name) VALUES(2, 'estebanexample');

INSERT INTO `sofia`.`emails` (address) VALUES ('esteban@cabezudo.net');
INSERT INTO `sofia`.`emails` (address) VALUES ('sofia@cabezudo.net');
INSERT INTO `sofia`.`emails` (address) VALUES ('sofia@example.com');
INSERT INTO `sofia`.`emails` (address) VALUES ('esteban@example.com');

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
INSERT INTO `authorities` (account_user_id, authority) VALUES (4, 'admin');

INSERT INTO `administrative_division_types` (name) VALUES ('state');
INSERT INTO `administrative_division_types` (name) VALUES ('municipality');
INSERT INTO `administrative_division_types` (name) VALUES ('city');
INSERT INTO `administrative_division_types` (name) VALUES ('colony');
INSERT INTO `administrative_division_types` (name) VALUES ('delegation');
INSERT INTO `administrative_division_types` (name) VALUES ('residentialDevelopment');

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

INSERT INTO `languages` (code) VALUES ('de');
INSERT INTO `languages` (code) VALUES ('en');
INSERT INTO `languages` (code) VALUES ('es');
INSERT INTO `languages` (code) VALUES ('fr');
INSERT INTO `languages` (code) VALUES ('it');
INSERT INTO `languages` (code) VALUES ('pt');

INSERT INTO `phone_formats` (format) VALUES ('## #### ####');

INSERT INTO `countries` (name, code, language_id, phone_country_code, phone_format_id) VALUES ('mexico', 'mx', 1, 52, 1);
