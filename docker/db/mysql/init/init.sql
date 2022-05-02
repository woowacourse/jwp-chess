CREATE TABLE room
(
    id       BIGINT      NOT NULL AUTO_INCREMENT,
    team     VARCHAR(50) NOT NULL,
    title    VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    status   BOOLEAN,
    UNIQUE (title),
    PRIMARY KEY (id)
);

CREATE TABLE board
(
    id       BIGINT      NOT NULL AUTO_INCREMENT,
    position VARCHAR(50) NOT NULL,
    symbol   VARCHAR(50) NOT NULL,
    room_id  BIGINT      NOT NULL,
    PRIMARY KEY (id),
    KEY room_id (room_id),
    CONSTRAINT id FOREIGN KEY (room_id) REFERENCES room (id) ON DELETE CASCADE ON UPDATE CASCADE
);
