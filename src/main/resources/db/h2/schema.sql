CREATE SCHEMA IF NOT EXISTS chess;
USE chess;


DROP TABLE IF EXISTS chess.room;
DROP TABLE IF EXISTS chess.user;
DROP TABLE IF EXISTS chess.chessgame;

CREATE TABLE IF NOT EXISTS chessgame (
    id INT NOT NULL AUTO_INCREMENT,
    running TINYINT NULL,
    pieces VARCHAR(192) NULL,
    next_turn VARCHAR(15) NULL,
    PRIMARY KEY (id))
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS user (
    user_id INT NOT NULL AUTO_INCREMENT,
    color VARCHAR(10) NULL,
    password VARCHAR(128) NULL,
    PRIMARY KEY (user_id)
    )
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS room (
    room_id INT NOT NULL AUTO_INCREMENT,
    room_name VARCHAR(100) NULL,
    game_id INT NULL,
    user1 INT NULL,
    user2 INT NULL,
    PRIMARY KEY (room_id),
    FOREIGN KEY room(room_id)
    REFERENCES chessgame(id),
    FOREIGN KEY (user1)
    REFERENCES user(user_id),
    FOREIGN KEY (user2)
    REFERENCES user(user_id)
    )
    ENGINE = InnoDB;