DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS board;

create table room
(
    id       int auto_increment primary key,
    state    varchar(20),
    title    varchar(40) not null,
    password varchar(60) not null
);

create table board
(
    room_id  int         not null,
    position varchar(5)  not null,
    symbol   varchar(10) not null,
    color    varchar(10) not null,
    primary key (position, room_id),
    constraint board_room_id_fk
        foreign key (room_id) references room (id)
            on update cascade on delete cascade
);
