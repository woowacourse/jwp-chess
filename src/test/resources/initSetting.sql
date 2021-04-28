CREATE TABLE IF NOT EXISTS pieces
(
    room_id    bigint,
    piece_name char(1),
    position   char(2)
    );
CREATE TABLE IF NOT EXISTS room
(
    id           bigint NOT NULL AUTO_INCREMENT,
    title        varchar(64) DEFAULT NULL,
    turn         char(5)     DEFAULT NULL,
    playing_flag tinyint(1) DEFAULT NULL,
    PRIMARY KEY (id)
    );
INSERT INTO room (title, turn, playing_flag) values ('hi', 'WHITE', true);
INSERT INTO pieces (room_id, piece_name, position) values (1, 'p', 'a8');