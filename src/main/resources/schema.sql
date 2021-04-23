DROP TABLE IF EXISTS board;
DROP TABLE IF EXISTS game;

create table game
(
    room_name varchar(12) not null,
    room_number int not null auto_increment primary key,
    turn varchar(10) not null
);

create table board
(
    position varchar(12) not null,
    piece varchar(12) not null,
    room_number int,
    foreign key (room_number) references game (room_number)
);