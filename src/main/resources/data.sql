CREATE TABLE `game` (
  `id` int NOT NULL AUTO_INCREMENT,
  `userId` int unsigned DEFAULT NULL,
  `isEnd` tinyint DEFAULT NULL,
  `createdTime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;

CREATE TABLE `game_history` (
  `id` int NOT NULL AUTO_INCREMENT,
  `gameId` int DEFAULT NULL,
  `command` varchar(45) DEFAULT NULL,
  `createdTime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8;
