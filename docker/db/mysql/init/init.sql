create table room
(
    id        bigint      not null primary key auto_increment,
    name      varchar(20) not null,
    team      varchar(10) not null,
    game_over boolean     not null
);

CREATE TABLE board
(
    id       bigint      not null primary key auto_increment,
    room_id  bigint      not null,
    position varchar(20) not null,
    piece    varchar(20) not null,
    FOREIGN KEY (room_id) REFERENCES room (id)
);
