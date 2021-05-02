CREATE TABLE game
(
    game_id int(11) NOT NULL AUTO_INCREMENT,
    is_end tinyint(1) NOT NULL DEFAULT '0',
    PRIMARY KEY (game_id)
);

CREATE TABLE `piece` (
                         `piece_id` int(11) NOT NULL AUTO_INCREMENT,
                         `name` varchar(21) NOT NULL,
                         `color` varchar(21) NOT NULL,
                         `position` varchar(10) NOT NULL,
                         `game_id` int(11) NOT NULL,
                         PRIMARY KEY (`piece_id`),
                         KEY `piece_ibfk_1` (`game_id`),
                         CONSTRAINT `piece_ibfk_1` FOREIGN KEY (`game_id`) REFERENCES `game` (`game_id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `team` (
                        `team_id` int(11) NOT NULL AUTO_INCREMENT,
                        `name` varchar(21) NOT NULL,
                        `is_turn` tinyint(1) NOT NULL,
                        `game_id` int(11) NOT NULL,
                        PRIMARY KEY (`team_id`),
                        KEY `team_ibfk_1` (`game_id`),
                        CONSTRAINT `team_ibfk_1` FOREIGN KEY (`game_id`) REFERENCES `game` (`game_id`) ON DELETE CASCADE ON UPDATE CASCADE
);



CREATE TABLE `room` (
                        `room_id` int(11) NOT NULL AUTO_INCREMENT,
                        `name` varchar(21) NOT NULL,
                        `pw` varchar(21) NOT NULL,
                        `white_player` varchar(45) DEFAULT NULL,
                        `black_player` varchar(45) DEFAULT NULL,
                        `game_id` int(11) NOT NULL,
                        PRIMARY KEY (`room_id`),
                        UNIQUE KEY `white_player_UNIQUE` (`white_player`),
                        KEY `white_idx` (`white_player`),
                        KEY `black_idx` (`black_player`),
                        KEY `room_ibfk_1` (`game_id`),
                        CONSTRAINT `room_ibfk_1` FOREIGN KEY (`game_id`) REFERENCES `game` (`game_id`) ON DELETE CASCADE ON UPDATE CASCADE,
);

CREATE TABLE `user` (
                        `user_id` int(11) NOT NULL AUTO_INCREMENT,
                        `user_name` varchar(45) NOT NULL,
                        `user_pw` varchar(45) DEFAULT NULL,
                        `room_id` int(11) DEFAULT NULL,
                        PRIMARY KEY (`user_id`),
                        UNIQUE KEY `user_name_UNIQUE` (`user_name`),
                        KEY `room_id_idx` (`room_id`),
                        CONSTRAINT `room_id` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`) ON DELETE SET NULL ON UPDATE NO ACTION
);

