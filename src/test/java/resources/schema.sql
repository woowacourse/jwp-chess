DROP TABLE board IF EXISTS;
DROP TABLE room IF EXISTS;

create table room
(
    id int(10) NOT NULL AUTO_INCREMENT,
    title varchar(255),
    password varchar(255) NOT NULL,
    color varchar(5) NOT NULL DEFAULT 'WHITE',
    finished boolean default 0,
    deleted boolean default 0,
    primary key (id)
);

create table board
(
    board_id int(10) NOT NULL AUTO_INCREMENT,
    position varchar(2) NOT NULL,
    piece varchar(10) NOT NULL,
    room_id int(10) NOT NULL,
    primary key (board_id),
    foreign key (room_id) references room (id)
);
