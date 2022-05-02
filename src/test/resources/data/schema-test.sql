DROP TABLE IF EXISTS square;
DROP TABLE IF EXISTS room;

create table room
(
    id       bigint auto_increment primary key,
    state    varchar(20) not null,
    title    varchar(40) not null,
    password varchar(60) not null
);

create table square
(
    id       bigint auto_increment primary key,
    room_id  bigint      not null,
    position varchar(5)  not null,
    symbol   varchar(10) not null,
    color    varchar(10) not null,
    constraint square_room_id_fk
        foreign key (room_id) references room (id)
            on update cascade on delete cascade
);
