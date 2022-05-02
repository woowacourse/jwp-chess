drop table if exists piece;
drop table if exists chess_game;

create table chess_game
(
    id       int         not null auto_increment primary key,
    name     varchar(10) not null,
    password varchar(20) not null,
    turn     varchar(5)  not null,
    running  tinyint(1) not null
);

create table piece
(
    id            int        not null auto_increment primary key,
    position      varchar(2) not null,
    name          varchar(6) not null,
    team          varchar(5) not null,
    chess_game_id int        not null,
    foreign key (chess_game_id) references chess_game (id)
);
