DROP TABLE IF EXISTS room ;
DROP TABLE IF EXISTS game ;
DROP TABLE IF EXISTS piece ;

create table room (
    id bigint auto_increment  not null primary key,
    name varchar(255) not null,
    password varchar(20)  not null
);

create table piece (
    game bigint not null,
    name varchar(20) not null,
    color varchar(20) not null,
    position varchar(20) not null
);

create table game (
    room bigint not null,
    turn varchar(10) not null
);