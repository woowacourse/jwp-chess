drop table if exists piece cascade;
drop table if exists game cascade;
drop table if exists room cascade;

create table room
(
    no          int         not null auto_increment,
    title       varchar(50) not null,
    password    varchar(20) not null,
    primary key (no)
);

create table game
(
    no          int         not null auto_increment,
    room_no     int         not null,
    running     boolean     not null,
    white_turn  boolean     not null,
    primary key (no),
    foreign key (room_no) references room (no)
);

create table piece
(
    no       int        not null auto_increment,
    game_no  int        not null,
    position varchar(2) not null,
    type     varchar(6) not null,
    white    boolean    not null,
    primary key (no),
    unique key (game_no, position),
    foreign key (game_no) references game (no)
);
