create table board
(
    id int(10) not null,
    position varchar(2) not null,
    piece varchar(10) not null,
    constraint board_PK primary key (id, position),
    foreign key board_FK (id) REFERENCES room(id) on delete cascade
);

create table player
(
    id int(10) not null primary key,
    color varchar(5) not null,
    foreign key player_FK (id) REFERENCES room(id) on delete cascade
);

create table room
(
    id int(10) not null AUTO_INCREMENT primary key,
    name varchar(5) not null,
    password varchar(100) not null
);