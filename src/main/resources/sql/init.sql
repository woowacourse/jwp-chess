create table CHESSGAME
(
    id      bigint auto_increment
        primary key,
    game_name varchar(20) not null,
    turn      varchar(20) not null,
    password  varchar(20) not null
);

create table PIECE
(
    id      bigint auto_increment
        primary key,
    type        enum ('r', 'n', 'b', 'q', 'k', 'p') not null,
    team        enum ('white', 'black')             not null,
    `rank`      int                                 not null,
    file        varchar(1)                          not null,
    chessgame_id bigint                             null,
    constraint PIECE_CHESSBOARD__fk
        foreign key (chessgame_id) references CHESSGAME (id)
            on update cascade on delete cascade
);