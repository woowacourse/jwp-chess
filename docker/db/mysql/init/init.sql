CREATE TABLE room
(
    id       bigint      NOT NULL AUTO_INCREMENT,
    team     varchar(50) NOT NULL,
    title    varchar(50) NOT NULL,
    password varchar(50) NOT NULL,
    status   boolean,
    UNIQUE (title),
    PRIMARY KEY (id)
);

CREATE TABLE board
(
    id       bigint      NOT NULL AUTO_INCREMENT,
    position varchar(50) NOT NULL,
    symbol   varchar(50) NOT NULL,
    room_id  bigint      NOT NULL,
    PRIMARY KEY (id),
    KEY room_id (room_id),
    CONSTRAINT id FOREIGN KEY (room_id) REFERENCES room (id) ON DELETE CASCADE ON UPDATE CASCADE
);
