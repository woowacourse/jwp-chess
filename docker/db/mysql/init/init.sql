CREATE TABLE games
(
    game_id     int         not null AUTO_INCREMENT,
    password    varchar(25) not null,
    turn        varchar(5)  not null,
    primary key (game_id)
);

CREATE TABLE pieces
(
    piece_id    int         not null AUTO_INCREMENT,
    game_id     int         not null,
    position    varchar(4)  not null,
    name        varchar(10) not null,
    primary key (piece_id),
    foreign key(game_id) references GAMES(game_id)
);
