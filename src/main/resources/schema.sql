CREATE SCHEMA IF NOT EXISTS `chess` DEFAULT CHARACTER SET utf8 ;
USE `chess` ;

DROP TABLE IF EXISTS `chess`.`chessgame` ;

CREATE TABLE IF NOT EXISTS `chess`.`chessgame` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `running` TINYINT NOT NULL,
    `pieces` VARCHAR(192) NOT NULL,
    `next_turn` VARCHAR(15) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC) )
ENGINE = InnoDB;