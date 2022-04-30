create table game
(
    game_id   int         not null auto_increment,
    title     varchar(20) not null,
    password  varchar(20) not null,
    turn      varchar(20) not null,
    status    varchar(20) not null,
    primary key (game_id)
);

create table piece
(
    position varchar(5)  not null,
    type     varchar(10) not null,
    color    varchar(20) not null,
    game_id  int not null,
    primary key (position)
);
