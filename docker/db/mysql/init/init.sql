create table room
(
    id   int not null auto_increment primary key,
    name varchar(16) not null
);

create table board
(
    id        int not null auto_increment primary key,
    room_id int not null,
    turn      varchar(5) not null,
    foreign key (room_id) references room(id)
);

create table piece
(
    id       int not null auto_increment primary key,
    board_id int not null,
    position varchar(2) not null,
    color    varchar(5) not null,
    role     varchar(1) not null,
    foreign key (board_id) references board(id) on delete cascade
);
