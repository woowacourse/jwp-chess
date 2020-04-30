create database wootecochess;

create table room
(
    id bigint auto_increment primary key,
    name varchar(64) not null,
    unique(name)
);

create table game
(
    room bigint,
    turn int not null
);

create table cell
(
    game bigint not null,
    position varchar(64) not null,
    piece varchar(64) not null
)