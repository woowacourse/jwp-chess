USE chess;

CREATE TABLE IF NOT EXISTS room
(
    room_id BIGINT AUTO_INCREMENT,
    title varchar(10),
    PRIMARY KEY (room_id)
);

CREATE TABLE IF NOT EXISTS chess
(
    room_id BIGINT,
    chess_id BIGINT AUTO_INCREMENT,
    status   varchar(10),
    turn     varchar(10),
    PRIMARY KEY (chess_id),
    FOREIGN KEY (room_id) REFERENCES room (room_id)
);

CREATE TABLE IF NOT EXISTS piece
(
    piece_id BIGINT AUTO_INCREMENT,
    chess_id BIGINT,
    position varchar(10),
    color    varchar(10),
    name     varchar(10),
    PRIMARY KEY (piece_id),
    FOREIGN KEY (chess_id) REFERENCES chess (chess_id)
);
