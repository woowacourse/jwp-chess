CREATE TABLE room
(
    id           INT AUTO_INCREMENT,
    name         VARCHAR(10) NOT NULL,
    password     VARCHAR(10) NOT NULL,
    game_status  VARCHAR(10) NOT NULL,
    current_turn VARCHAR(10) NOT NULL,
    PRIMARY KEY (name)
);

CREATE TABLE chess_piece
(
    id          INT AUTO_INCREMENT,
    room_id     INT         NOT NULL,
    position    VARCHAR(10) NOT NULL,
    chess_piece VARCHAR(10) NOT NULL,
    color       VARCHAR(10) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (room_id) REFERENCES room (id) ON DELETE CASCADE
);
