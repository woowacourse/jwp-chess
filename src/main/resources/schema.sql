create table piece(
    id bigint auto_increment primary key,
    room_id varchar(32) not null,
    position varchar(2) not null,
    symbol varchar(2) not null
);

create table chessGame(
    id bigint auto_increment primary key,
    room_id varchar(32) not null,
    turn varchar(8) not null
);
