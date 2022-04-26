create table PIECE
(
    id     bigint auto_increment primary key,
    type   enum ('r', 'n', 'b', 'q', 'k', 'p') not null,
    team   enum ('white', 'black')             not null,
    `rank` int                                 not null,
    file   varchar(1)                          not null
);

create table CHESSGAME
(
    id bigint auto_increment primary key,
    game_name varchar(20) not null,
    turn      varchar(5)  not null
);

alter table PIECE
    add chessgame_id bigint null;

alter table PIECE
    add constraint PIECE_CHESSBOARD__fk
        foreign key (chessgame_id) references CHESSGAME (id)
            on update cascade on delete cascade;