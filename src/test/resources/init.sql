DROP TABLE gameDto IF EXISTS;
DROP TABLE piece IF EXISTS;

create table game
(
    id       int         not null auto_increment,
    title    varchar(30) not null,
    password varchar(30) not null,
    turn     varchar(20) not null,
    status   varchar(20) not null,
    primary key (id)
);

create table piece
(
    id       int         not null auto_increment,
    position varchar(5)  not null,
    type     varchar(10) not null,
    color    varchar(20) not null,
    game_id  int         not null,
    primary key (id),
    foreign key (game_id) references game (id)
);