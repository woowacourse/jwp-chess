DROP TABLE IF EXISTS chess_room;
CREATE TABLE chess_room
(
    room_no   int           not null auto_increment,
    room_name varchar(50)   not null,
    turn      varchar(10)   not null,
    board     varchar(1000) not null,
    primary key (room_no)
);