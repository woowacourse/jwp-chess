use chess;

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