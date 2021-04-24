DROP DATABASE if exists chess_db;
CREATE DATABASE chess_db;

USE chess_db;

DROP TABLE IF EXISTS game_room_info CASCADE;
DROP TABLE IF EXISTS chess_game CASCADE;
DROP TABLE IF EXISTS team_info CASCADE;

CREATE TABLE game_room_info (
    game_id int NOT NULL AUTO_INCREMENT,
    room_name VARCHAR(50),
    PRIMARY KEY (game_id)
);

CREATE TABLE chess_game (
    game_id int NOT NULL,
    current_turn_team VARCHAR(5) NOT NULL,
    is_playing boolean NOT NULL,
    FOREIGN KEY (game_id) REFERENCES game_room_info(game_id)
);

CREATE TABLE team_info (
    game_id int NOT NULL,
    team VARCHAR(5) NOT NULL,
    piece_info VARCHAR(400) NOT NULL,
    FOREIGN KEY (game_id) REFERENCES game_room_info(game_id)
);