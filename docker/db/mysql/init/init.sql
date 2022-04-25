create table piece
(
    id     bigint auto_increment primary key,
    type   enum ('r', 'n', 'b', 'q', 'k', 'p') not null,
    team   enum ('white', 'black')             not null,
    `rank` int                                 not null,
    file   varchar(1)                          not null
);

create table chessgame
(
    game_name varchar(20) not null,
    turn      varchar(5)  not null,
    constraint chessgame_pk
        primary key (game_name)
);

alter table piece
    add game_name varchar(20) null;

alter table piece
    add constraint piece_chessboard__fk
        foreign key (game_name) references chessgame (game_name)
            on update cascade on delete cascade;