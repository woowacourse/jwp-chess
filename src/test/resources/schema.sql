-- use chess;

DROP TABLE IF EXISTS piece;
DROP TABLE IF EXISTS room;

CREATE TABLE IF NOT EXISTS room (
    room_id int(11) NOT NULL AUTO_INCREMENT,
    current_turn char(5) DEFAULT NULL,
    room_name varchar(20) NOT NULL,
    PRIMARY KEY(room_id)
    ) DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS piece (
    piece_name char(1) DEFAULT NULL,
    piece_position char(2) DEFAULT NULL,
    room_id int(11) NOT NULL,
    CONSTRAINT room_id FOREIGN KEY (room_id) REFERENCES room (room_id)
    ) DEFAULT CHARSET=utf8;

INSERT INTO ROOM(room_id, current_turn, room_name) values(1, 'white', 'room1');
INSERT INTO ROOM(room_id, current_turn, room_name) values(2, 'white', 'room2');
INSERT INTO ROOM(room_id, current_turn, room_name) values(3, 'white', 'room3');

INSERT INTO piece(piece_name, piece_position, room_id) values('p', 'e2', 1);