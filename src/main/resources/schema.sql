DROP TABLE IF EXISTS commands;
DROP TABLE IF EXISTS ChessRoom;

CREATE TABLE commands (
    command_id int PRIAMRY KEY AUTO_INCREMENT NOT NULL,
    command varchar(128) NOT NULL;

);

CREATE TABLE ChessRoom (
    room_id int PRIMARY KEY AUTO_INCREMENT NOT NULL,
    room_name varchar(128) NOT NULL;
)