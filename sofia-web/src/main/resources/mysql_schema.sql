USE `sofia`;
DROP TABLE IF EXISTS `web_clients`;

CREATE TABLE `web_clients` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `language` VARCHAR(5),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
