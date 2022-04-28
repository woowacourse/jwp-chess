CREATE TABLE room
(
    id       VARCHAR(36) NOT NULL,
    title    VARCHAR(50) NOT NULL,
    password VARCHAR(64) NOT NULL,
    turn     ENUM('WHITE', 'BLACK'),
    PRIMARY KEY (id)
);

CREATE TABLE board
(
    room_id     VARCHAR(36) NOT NULL,
    x_axis      ENUM('1', '2', '3', '4', '5', '6', '7', '8'),
    y_axis      ENUM('1', '2', '3', '4', '5', '6', '7', '8'),
    piece_type  ENUM('PAWN', 'ROOK', 'KNIGHT', 'BISHOP', 'QUEEN', 'KING'),
    piece_color ENUM('WHITE', 'BLACK'),
    PRIMARY KEY (room_id, x_axis, y_axis),
    FOREIGN KEY (room_id) REFERENCES room (id) ON DELETE CASCADE
);
