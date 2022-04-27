create table game
(
    id             int          not null unique auto_increment,
    turn           varchar(10)  not null default 'black',
    end_flag tinyint(1) not null default false,
    title          varchar(100) not null,
    password       varchar(100) not null
);

create table piece
(
    id       int         not null unique auto_increment,
    name     varchar(10) not null,
    color    varchar(10) not null,
    position varchar(10) not null,
    game_id  int         not null,
    foreign key (game_id) references game (id)
);
