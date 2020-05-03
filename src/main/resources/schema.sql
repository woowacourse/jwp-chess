DROP TABLE IF EXISTS move_command;
DROP TABLE IF EXISTS chess_room;

CREATE TABLE move_command (
    command_id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    chess_room INT NOT NULL,
    chess_room_key INT,
    command VARCHAR(128) NOT NULL
);

CREATE TABLE chess_room (
    room_id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    room_name VARCHAR(128) NOT NULL
);