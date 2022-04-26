create table chess_game
(
    id        int not null AUTO_INCREMENT,
    game_name varchar(20) not null,
    password  varchar(20) not null,
    turn      varchar(5)  not null,
    primary key (id)
);

create table piece
(
    id              bigint auto_increment               not null,
    type            enum ('r', 'n', 'b', 'q', 'k', 'p') not null,
    team            enum ('white', 'black')             not null,
    `rank`          int                                 not null,
    file            varchar(1)                          not null,
    chess_game_id   int                                 not null,
    primary key (id),
    foreign key (chess_game_id) references chess_game (id) on delete cascade
);
