create table room
(
    room_name varchar(12) not null,
    room_id int not null auto_increment primary key,
    turn varchar(10) not null
);

create table board
(
    position varchar(12) not null,
    piece varchar(12) not null,
    room_id int,
    foreign key (room_id) references room (room_id)
);