drop table if exists piece cascade;
drop table if exists game cascade;

create table game
(
    id         bigint     not null auto_increment,
    title       varchar(10) not null,
    password    varchar(10) not null,
    white_turn boolean not null,
    finished boolean not null,
    primary key (id)
);

create table piece
(
    id       bigint        not null auto_increment,
    game_id  bigint        not null,
    position varchar(2) not null,
    type     varchar(6) not null,
    white    boolean    not null,
    primary key (id),
    unique key (game_id, position),
    foreign key (game_id) references game (id)
);
