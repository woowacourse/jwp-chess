DROP TABLE board IF EXISTS;

create table board
(
    position varchar(10) not null,
    piece    varchar(20) not null,
    primary key (position)
);

DROP TABLE game_status IF EXISTS;

create table game_status
(
    status varchar(10) not null,
    primary key (status)
);

DROP TABLE turn IF EXISTS;

create table turn
(
    team varchar(10) not null,
    primary key (team)
);

DROP TABLE room IF EXISTS;

create table room
(
    id int(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(20) not null,
    pw varchar(20) not null ,
    primary key (id)
);



