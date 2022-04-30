drop table room if exists;
drop table board if exists;
create table room
(
    id        bigint       not null primary key auto_increment,
    password  varchar(100) not null,
    name      varchar(20)  not null,
    team      varchar(10)  not null,
    game_over boolean      not null
);

CREATE TABLE board
(
    id       bigint      not null primary key auto_increment,
    room_id  bigint      not null,
    position varchar(20) not null,
    piece    varchar(20) not null
);
