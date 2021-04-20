CREATE TABLE `game` (
  `id` int NOT NULL AUTO_INCREMENT,
  `userId` int unsigned DEFAULT NULL,
  `isEnd` tinyint DEFAULT NULL,
  `createdTime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `game_history` (
  `id` int NOT NULL AUTO_INCREMENT,
  `gameId` int DEFAULT NULL,
  `command` varchar(45) DEFAULT NULL,
  `createdTime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
