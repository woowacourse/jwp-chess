create table if not exists player
(
    id       int         not null auto_increment,
    username varchar(15) not null,
    password varchar(60) not null,
    win      int,
    lose     int,
    draw     int,
    primary key (id)
);

create table if not exists game
(
    id    varchar(255) not null primary key,
    title varchar(255) not null,
    white int          not null,
    black int          not null,
    constraint game_ibfk_1 foreign key (white) references player (id),
    constraint game_ibfk_2 foreign key (black) references player (id)
);

create index black
    on game (black);

create index white
    on game (white);

create table if not exists move
(
    id             int auto_increment primary key,
    game           varchar(255) not null,
    start_position varchar(2)   not null,
    end_position   varchar(2)   not null,
    constraint move_ibfk_1 foreign key (game) references game (id)
);

create index game on move (game);
