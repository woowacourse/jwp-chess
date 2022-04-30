DROP TABLE IF EXISTS board;
DROP TABLE IF EXISTS game;

CREATE TABLE game
(
    id       int auto_increment
        primary key,
    title    varchar(20) not null,
    password varchar(20) not null,
    state    varchar(20) not null
);

CREATE TABLE board
(
    position varchar(5)  not null,
    symbol   varchar(10) not null,
    color    varchar(10) not null,
    game_id  int         not null,
    primary key (position, game_id),
    constraint board_game_id_fk
        foreign key (game_id) references game (id)
            on update cascade on delete cascade
);
