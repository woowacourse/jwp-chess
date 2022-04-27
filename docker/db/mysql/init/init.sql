USE chess;

CREATE TABLE rooms
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(10)  NOT NULL,
    password VARCHAR(100) NOT NULL,
    status   VARCHAR(10)  NOT NULL,
    turn     VARCHAR(10)  NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE pieces
(
    id       BIGINT      NOT NULL AUTO_INCREMENT,
    room_id  BIGINT      NOT NULL,
    position VARCHAR(10) NOT NULL,
    type     VARCHAR(10) NOT NULL,
    color    VARCHAR(10) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (room_id) REFERENCES rooms (id) ON DELETE CASCADE
);
