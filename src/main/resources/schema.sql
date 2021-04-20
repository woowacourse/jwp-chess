CREATE TABLE pieces
(
    room_id    bigint,
    piece_name char(1),
    position   char(2)
);
CREATE TABLE room
(
    id           bigint,
    turn         char(5),
    playing_flag boolean
);