create table board
(
    position varchar(2) not null,
    piece varchar(10) not null,
    primary key (position)
);

create table player
(
    color varchar(5) not null,
    primary key (color)
);

create table room
(
    num int(10) not null AUTO_INCREMENT primary key,
    name varchar(5) not null,
    password varchar(100) not null
);