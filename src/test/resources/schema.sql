DROP TABLE piece IF EXISTS;
DROP TABLE game IF EXISTS;
DROP TABLE room IF EXISTS;

CREATE TABLE room (
    id          BIGINT          AUTO_INCREMENT,
    room_name   VARCHAR(100)    NOT NULL UNIQUE,
    password    VARCHAR(100)    NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE game (
    id          BIGINT      AUTO_INCREMENT,
    turn_color  VARCHAR(10) NOT NULL,
    state       VARCHAR(10) NOT NULL,
    room_id     BIGINT      NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (room_id) REFERENCES room (id) ON DELETE CASCADE
);

CREATE TABLE piece (
    piece_type          VARCHAR(10) NOT NULL,
    piece_color         VARCHAR(10) NOT NULL,
    vertical_index      INT         NOT NULL,
    horizontal_index    INT         NOT NULL,
    game_id             BIGINT      NOT NULL,
    FOREIGN KEY(game_id) REFERENCES game (id) ON DELETE CASCADE
);