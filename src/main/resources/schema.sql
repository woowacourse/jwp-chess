CREATE SCHEMA IF NOT EXISTS `chess` DEFAULT CHARACTER SET utf8 ;
USE `chess` ;

DROP TABLE IF EXISTS chess.room;
DROP TABLE IF EXISTS chess.user;
DROP TABLE IF EXISTS chess.chessgame;

CREATE TABLE IF NOT EXISTS chess.chessgame (
    id INT NOT NULL AUTO_INCREMENT,
    running TINYINT NULL,
    pieces VARCHAR(192) NULL,
    next_turn VARCHAR(15) NULL,
    PRIMARY KEY (id)
    )
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS chess.user (
    user_id INT NOT NULL AUTO_INCREMENT,
    color VARCHAR(10) NULL,
    password VARCHAR(128) NULL,
    PRIMARY KEY (user_id)
    )
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS chess.room (
    room_id INT NOT NULL AUTO_INCREMENT ,
    game_id INT NOT NULL,
    room_name varchar(100) NOT NULL,
    user1 INT NOT NULL,
    user2 INT NULL,
    PRIMARY KEY (room_id),
    FOREIGN KEY room(room_id)
    REFERENCES chess.chessgame(id),
    FOREIGN KEY (user1)
    REFERENCES chess.user(user_id),
    FOREIGN KEY (user2)
    REFERENCES chess.user(user_id)
    )
    ENGINE = InnoDB;