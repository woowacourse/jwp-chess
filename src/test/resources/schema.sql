CREATE TABLE room
(
    id           INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name         VARCHAR(10) NOT NULL,
    password     VARCHAR(10) NOT NULL,
    game_status  VARCHAR(10) NOT NULL,
    current_turn VARCHAR(10) NOT NULL
);

CREATE TABLE chess_piece
(
    id          INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    room_id     INT         NOT NULL,
    position    VARCHAR(10) NOT NULL,
    chess_piece VARCHAR(10) NOT NULL,
    color       VARCHAR(10) NOT NULL,
    FOREIGN KEY (room_id) REFERENCES Room (id) ON DELETE CASCADE
);
