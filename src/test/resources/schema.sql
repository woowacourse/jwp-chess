-- SET mode mysql;

DROP TABLE IF EXISTS board;
DROP TABLE IF EXISTS room;

CREATE TABLE room (
    room_id BIGINT NOT NULL,
    turn VARCHAR(16) NOT NULL,
    is_playing BOOLEAN NOT NULL,
    name VARCHAR(16) NOT NULL,
    password VARCHAR(16),
    create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(room_id)
);

CREATE TABLE board (
    board_id BIGINT NOT NULL AUTO_INCREMENT,
    team VARCHAR(16) NOT NULL,
    position VARCHAR(16) NOT NULL,
    piece VARCHAR(16) NOT NULL,
    is_first_moved BOOLEAN NOT NULL,
    room_id BIGINT NOT NULL,
    PRIMARY KEY(board_id),
    FOREIGN KEY (room_id) REFERENCES room (room_id)
);