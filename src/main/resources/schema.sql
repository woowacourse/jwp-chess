DROP TABLE IF EXISTS board;
DROP TABLE IF EXISTS game;

create table game(
    room_name varchar(20) not null primary key,
    turn_owner varchar(10) not null
);

create table board
(
    seq int not null auto_increment primary key,
    position varchar(12) not null,
    piece    varchar(12) not null,
    room_name varchar(20) not null,
    foreign key (room_name) references game (room_name) on update cascade
);