create table board
(
    id bigint auto_increment primary key,
);

create table piece
(
    board_entity bigint not null,
    position varchar(2) not null,
    name varchar(1)  not null
);

create table turn
(
    board_entity bigint not null,
    is_white_turn tinyint(1) not null
);