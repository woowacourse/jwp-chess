CREATE TABLE `grid` (
                        `gridId` int NOT NULL AUTO_INCREMENT,
                        `isBlackTurn` tinyint NOT NULL,
                        `isFinished` tinyint NOT NULL DEFAULT '0',
                        `roomId` int NOT NULL,
                        `createdAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        `isStarted` tinyint NOT NULL DEFAULT '0',
                        PRIMARY KEY (`gridId`)
) ENGINE=InnoDB AUTO_INCREMENT DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin

CREATE TABLE `piece` (
                         `pieceId` int NOT NULL AUTO_INCREMENT,
                         `isBlack` tinyint NOT NULL,
                         `position` varchar(45) COLLATE utf8mb4_bin NOT NULL,
                         `gridId` int NOT NULL,
                         `name` varchar(45) COLLATE utf8mb4_bin NOT NULL,
                         PRIMARY KEY (`pieceId`)
) ENGINE=InnoDB AUTO_INCREMENT DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin

CREATE TABLE `room` (
                        `roomId` int NOT NULL AUTO_INCREMENT,
                        `roomName` varchar(45) COLLATE utf8mb4_bin NOT NULL,
                        `createdAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        PRIMARY KEY (`roomId`)
) ENGINE=InnoDB AUTO_INCREMENT DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin