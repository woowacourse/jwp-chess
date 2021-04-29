CREATE TABLE IF NOT EXISTS `mychess`.`room` (
    `room_id` BIGINT NOT NULL AUTO_INCREMENT,
    `room_name` VARCHAR(45) NOT NULL,
    `current_turn` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`room_id`)
) CHARSET = utf8, ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `mychess`.`piece` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `piece_name` VARCHAR(45) NOT NULL,
    `piece_position` VARCHAR(45) NOT NULL,
    `room_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`room_id`)
    REFERENCES room(`room_id`)
) CHARSET = utf8, ENGINE = InnoDB;