CREATE TABLE games
(
    game_id  int         not null AUTO_INCREMENT,
    name     varchar(64) not null,
    password varchar(64) not null,
    turn     varchar(5)  not null,
    primary key (game_id)
);

CREATE TABLE pieces
(
    piece_id int         not null AUTO_INCREMENT,
    position varchar(4)  not null,
    name     varchar(10) not null,
    game_id int not null,
    primary key (piece_id),
    foreign key(game_id) references GAMES (game_id)
);
