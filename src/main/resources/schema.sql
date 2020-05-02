-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema wooteco.chess
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema wooteco.chess
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `chess` DEFAULT CHARACTER SET utf8;
USE `chess`;

-- -----------------------------------------------------
-- Table `chess`.`room`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chess`.`room`
(
    `id`            BIGINT         NOT NULL,
    `black_password` VARCHAR(21)    NOT NULL,
    `white_password` VARCHAR(21)    NOT NULL,
    `is_end`        BIT     NOT NULL,
    `name`          VARCHAR(21) NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `chess`.`move`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chess`.`move`
(
    `id`      BIGINT        NOT NULL,
    `room_id` BIGINT        NOT NULL,
    `source`  VARCHAR(2) NOT NULL,
    `target`  VARCHAR(2) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `room_id_idx` (`room_id` ASC),
    CONSTRAINT `room_id`
        FOREIGN KEY (`room_id`)
            REFERENCES `chess`.`room` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
