DROP TABLE PIECES IF EXISTS;

CREATE TABLE pieces
(
    piece_id    int         not null AUTO_INCREMENT,
    game_id     int         not null,
    position    varchar(4)  not null,
    name        varchar(10) not null,
    primary key (piece_id)
);