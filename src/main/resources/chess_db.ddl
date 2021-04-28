CREATE TABLE pieces
(
    id         bigint(20) not null auto_increment primary key,
    room_id    bigint(20),
    piece_name char(1),
    position   char(2)
);

CREATE TABLE room
(
    id           bigint(20) primary key,
    turn         char(5),
    playing_flag boolean
);