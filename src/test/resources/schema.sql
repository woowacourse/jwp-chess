DROP TABLE IF EXISTS chess_piece;

DROP TABLE IF EXISTS room;

CREATE TABLE room
(
    room_id      INT PRIMARY KEY AUTO_INCREMENT,
    name         VARCHAR(10)  NOT NULL,
    game_status  VARCHAR(10)  NOT NULL,
    current_turn VARCHAR(10)  NOT NULL,
    password     VARCHAR(255) NOT NULL,
    is_delete    BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE TABLE chess_piece
(
    chess_piece_id INT PRIMARY KEY AUTO_INCREMENT,
    room_id        INT         NOT NULL,
    position       VARCHAR(10) NOT NULL,
    chess_piece    VARCHAR(10) NOT NULL,
    color          VARCHAR(10) NOT NULL,
    FOREIGN KEY (room_id) REFERENCES room (room_id) ON DELETE CASCADE
);
