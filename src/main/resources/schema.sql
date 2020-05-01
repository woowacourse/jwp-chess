create table chess_game_table
(
    id   bigint auto_increment primary key,
    turn varchar(5) not null
);

create table board_table
(
    position         varchar(2) not null,
    piece            varchar(6) not null,
    team             varchar(5) not null,
    chess_game_table bigint     not null
);
