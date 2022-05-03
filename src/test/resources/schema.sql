drop table if exists piece;
drop table if exists game;


create table game
(
    id       int AUTO_INCREMENT PRIMARY KEY,
    turn     varchar(10)  not null default 'white',
    end_flag tinyint(1) not null default false,
    title    varchar(100) not null,
    password varchar(100) not null
);

create table piece
(
    id       int AUTO_INCREMENT PRIMARY KEY,
    name     varchar(10) not null,
    color    varchar(10) not null,
    position varchar(10) not null,
    game_id  int         not null,
    foreign key (game_id) references game (id) on delete cascade
);