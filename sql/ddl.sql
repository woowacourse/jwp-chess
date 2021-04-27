DROP DATABASE if exists chess_db;
CREATE DATABASE chess_db DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE chess_db;

DROP TABLE IF EXISTS game_room_info CASCADE;
DROP TABLE IF EXISTS chess_game_info CASCADE;
DROP TABLE IF EXISTS team_info CASCADE;

CREATE TABLE game_room_info (
    room_id int NOT NULL AUTO_INCREMENT,
    room_name VARCHAR(50),
    PRIMARY KEY (room_id)
);

CREATE TABLE chess_game_info (
    room_id int NOT NULL,
    current_turn_team VARCHAR(5) NOT NULL,
    is_playing boolean NOT NULL,
    FOREIGN KEY (room_id) REFERENCES game_room_info(room_id)
);

CREATE TABLE team_info (
    room_id int NOT NULL,
    team VARCHAR(5) NOT NULL,
    piece_info VARCHAR(400) NOT NULL,
    FOREIGN KEY (room_id) REFERENCES game_room_info(room_id)
);