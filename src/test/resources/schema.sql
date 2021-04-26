CREATE TABLE pieces
(
    room_id    bigint,
    piece_name char(1),
    position   char(2)
);
CREATE TABLE room
(
    id           bigint NOT NULL AUTO_INCREMENT,
    title        varchar(64) DEFAULT NULL,
    turn         char(5)     DEFAULT NULL,
    playing_flag tinyint(1) DEFAULT NULL,
    PRIMARY KEY (id)
);

