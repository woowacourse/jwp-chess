CREATE TABLE room
(
    id bigint NOT NULL AUTO_INCREMENT,
    status varchar(50) NOT NULL,
    name varchar(50) NOT NULL,
    password varchar(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE board
(
    id bigint NOT NULL AUTO_INCREMENT,
    position varchar(50) NOT NULL,
    symbol varchar(50) NOT NULL,
    room_id bigint NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT id FOREIGN KEY (room_id) REFERENCES room (id) ON DELETE CASCADE ON UPDATE CASCADE
);