create table Game
(
    id       bigint auto_increment primary key,
    title    varchar(45) not null,
    password varchar(45) not null,
    status   varchar(45) not null,
    turn     varchar(45) not null
);

create table Board
(
    id       bigint auto_increment primary key not null,
    game_id  bigint                            not null,
    position varchar(5)                        not null,
    piece    varchar(20)                       not null,
    color    varchar(15)                       not null,
    foreign key (game_id) references game (id)
);
