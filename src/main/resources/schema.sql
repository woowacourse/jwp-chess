create table board
(
    id     int(10)     not null auto_increment primary key,
    status varchar(10) not null,
    team   varchar(10) not null
);

create table square
(
    id          int(10) not null auto_increment primary key,
    square_file int(10) not null,
    square_rank int(10) not null,
    board_id    int(10) not null,
    foreign key (board_id) references board (id)
        on delete cascade
);

create table piece
(
    id        int(10)     not null auto_increment primary key,
    type      varchar(10) not null,
    team      varchar(10) not null,
    square_id int(10)     not null,
    foreign key (square_id) references square (id)
        on delete cascade
);

create table room
(
    id       int(10)     not null auto_increment primary key,
    title    varchar(20) not null,
    password varchar(20) not null,
    board_id int(10)     not null,
    foreign key (board_id) references board (id)
        on delete cascade
);

create table member
(
    id      int(10)     not null auto_increment primary key,
    name    varchar(20) not null,
    room_id int(10)     not null,
    foreign key (room_id) references room (id)
        on delete cascade
);
