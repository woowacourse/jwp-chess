create table Game
(
    id       int         not null auto_increment primary key,
    title    varchar(45) not null,
    password varchar(20) not null,
    state    varchar(10) not null
);

create table piece
(
    game_id int not null,
    piece_type varchar(1) not null,
    position   varchar(2) not null,
    color      varchar(5) not null,
    foreign key (game_id) references Game(id) on delete cascade
);

