DROP DATABASE if exists chess_db;
CREATE DATABASE chess_db;

USE chess_db;

DROP TABLE IF EXISTS chess_game CASCADE;
DROP TABLE IF EXISTS team_info CASCADE;

CREATE TABLE chess_game (
    current_turn_team VARCHAR(5) NOT NULL,
    is_playing boolean NOT NULL
);

CREATE TABLE team_info (
    team VARCHAR(5) NOT NULL,
    piece_info VARCHAR(400) NOT NULL,
    primary key (team)
);