DROP TABLE room, board IF EXISTS;

DROP TABLE room IF EXISTS;

CREATE TABLE room
(
    id       VARCHAR(36) NOT NULL,
    title    VARCHAR(50) NOT NULL,
    password VARCHAR(64) NOT NULL,
    turn     ENUM('WHITE', 'BLACK'),
    PRIMARY KEY (id)
);
