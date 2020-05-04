create table if not exists piece2
(
    id                bingint auto_increment,
    name              varchar(244) not null,
    piece_row         varchar(244) not null,
    piece_col         varchar(244) not null,
    chess_game_entity int,
    primary key (id)
);

create table if not exists chessgame2
(
    id            int auto_increment,
    turn_is_black boolean not null,
    game_name varchar(244) not null,
    primary key (id)
);

