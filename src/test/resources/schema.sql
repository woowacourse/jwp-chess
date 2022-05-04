drop table chess_game if exists;
drop table piece if exists;

create table room
(
    id       bigint      not null auto_increment primary key,
    name     varchar(10) not null,
    password varchar(20) not null,
    turn     varchar(5)  not null
);

create table piece
(
    position varchar(2) not null,
    name     varchar(6) not null,
    team     varchar(5) not null,
    room_id  bigint     not null,
    foreign key (room_id) references room (id) on delete cascade
);

insert into room (name, password, turn) values ('rex_game', '111','화이트');
insert into room (name, password, turn) values ('rex_game2', '111','화이트');
