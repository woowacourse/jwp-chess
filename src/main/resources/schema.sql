create table chess_game
(
    id      bigint auto_increment primary key,
    room_id varchar(32) not null,
    turn    varchar(8)  not null
);

create table piece
(
    id   bigint auto_increment primary key,
    position   varchar(2),
    symbol     varchar(16) not null,
    chess_game bigint
);
