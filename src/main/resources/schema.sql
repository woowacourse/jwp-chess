create table piece(
    id bigint auto_increment primary key,
    board_id bigint not null,
    position varchar(2) not null,
    symbol varchar(2) not null
);

create table board(
    id bigint auto_increment primary key
);

create table chessGame(
    room_id varchar(32) primary key,
    board bigint not null,
    turn varchar(8) not null
);
