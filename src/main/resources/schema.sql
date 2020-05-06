create table room
(
    id bigint auto_increment primary key,
    title VARCHAR(45) NULL DEFAULT NULL ;
);

create table piece
(
    room_entity bigint not null,
    position varchar(2) not null,
    name varchar(1)  not null
);

create table turn
(
    room_entity bigint not null,
    is_white_turn tinyint(1) not null
);