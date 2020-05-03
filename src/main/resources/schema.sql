CREATE TABLE piece
(
    position    char(2)    NOT NULL,
    type        char(2)    NOT NULL,
    team        varchar(6) NOT NULL,
    room_entity bigint(20) NOT NULL,
    PRIMARY KEY (position, room_entity)
);

CREATE TABLE room
(
    id   bigint(20)  NOT NULL AUTO_INCREMENT,
    name varchar(20) NOT NULL,
    turn varchar(6)  NOT NULL,
    PRIMARY KEY (id)
);