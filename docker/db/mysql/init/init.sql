CREATE TABLE room
(
    id       long        not null auto_increment primary key,
    name     varchar(30) not null,
    password varchar(20) not null
);

CREATE TABLE turn
(
    id     long       not null auto_increment primary key,
    roomId long       not null,
    team   varchar(5) not null,
    constraint fk_roomId foreign key (roomId) references room (id) on delete cascade
);

insert into turn (team)
values ('WHITE');

CREATE TABLE piece
(
    id       long       not null auto_increment primary key,
    roomId   long       not null,
    position varchar(3) not null,
    name     varchar(2) not null,
    team     varchar(5) not null,
    constraint fk_roomId foreign key (roomId) references room (id) on delete cascade
);