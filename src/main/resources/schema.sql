DROP TABLE IF EXISTS board;
DROP TABLE IF EXISTS game;

create table game
(
    room_name varchar(12) not null,
    room_number int not null auto_increment primary key,
    turn varchar(10) not null
);

create table board
(
    position varchar(12) not null,
    piece varchar(12) not null,
    room_number int,
    foreign key (room_number) references game (room_number)
);

insert into game (room_name, turn)
values ('room_one', 'white');

insert into board (position, piece, room_number)
values ('a1', '&#9814;', 1);
insert into board (position, piece, room_number)
values ('b1', '&#9816;', 1);
insert into board (position, piece, room_number)
values ('c1', '&#9815;', 1);
insert into board (position, piece, room_number)
values ('d1', '&#9812;', 1);
insert into board (position, piece, room_number)
values ('e1', '&#9813;', 1);
insert into board (position, piece, room_number)
values ('f1', '&#9815;', 1);
insert into board (position, piece, room_number)
values ('g1', '&#9816;', 1);
insert into board (position, piece, room_number)
values ('h1', '&#9814;', 1);

insert into board (position, piece, room_number)
values ('a8', '&#9820;', 1);
insert into board (position, piece, room_number)
values ('b8', '&#9822;', 1);
insert into board (position, piece, room_number)
values ('c8', '&#9821;', 1);
insert into board (position, piece, room_number)
values ('d8', '&#9818;', 1);
insert into board (position, piece, room_number)
values ('e8', '&#9819;', 1);
insert into board (position, piece, room_number)
values ('f8', '&#9821;', 1);
insert into board (position, piece, room_number)
values ('g8', '&#9822;', 1);
insert into board (position, piece, room_number)
values ('h8', '&#9820;', 1);

insert into board (position, piece, room_number)
values ('a7', '&#9823;', 1);
insert into board (position, piece, room_number)
values ('b7', '&#9823;', 1);
insert into board (position, piece, room_number)
values ('c7', '&#9823;', 1);
insert into board (position, piece, room_number)
values ('d7', '&#9823;', 1);
insert into board (position, piece, room_number)
values ('e7', '&#9823;', 1);
insert into board (position, piece, room_number)
values ('f7', '&#9823;', 1);
insert into board (position, piece, room_number)
values ('g7', '&#9823;', 1);
insert into board (position, piece, room_number)
values ('h7', '&#9823;', 1);

insert into board (position, piece, room_number)
values ('a2', '&#9817;', 1);
insert into board (position, piece, room_number)
values ('b2', '&#9817;', 1);
insert into board (position, piece, room_number)
values ('c2', '&#9817;', 1);
insert into board (position, piece, room_number)
values ('d2', '&#9817;', 1);
insert into board (position, piece, room_number)
values ('e2', '&#9817;', 1);
insert into board (position, piece, room_number)
values ('f2', '&#9817;', 1);
insert into board (position, piece, room_number)
values ('g2', '&#9817;', 1);
insert into board (position, piece, room_number)
values ('h2', '&#9817;', 1);

insert into board (position, piece, room_number)
values ('a3', '', 1);
insert into board (position, piece, room_number)
values ('b3', '', 1);
insert into board (position, piece, room_number)
values ('c3', '', 1);
insert into board (position, piece, room_number)
values ('d3', '', 1);
insert into board (position, piece, room_number)
values ('e3', '', 1);
insert into board (position, piece, room_number)
values ('f3', '', 1);
insert into board (position, piece, room_number)
values ('g3', '', 1);
insert into board (position, piece, room_number)
values ('h3', '', 1);

insert into board (position, piece, room_number)
values ('a4', '', 1);
insert into board (position, piece, room_number)
values ('b4', '', 1);
insert into board (position, piece, room_number)
values ('c4', '', 1);
insert into board (position, piece, room_number)
values ('d4', '', 1);
insert into board (position, piece, room_number)
values ('e4', '', 1);
insert into board (position, piece, room_number)
values ('f4', '', 1);
insert into board (position, piece, room_number)
values ('g4', '', 1);
insert into board (position, piece, room_number)
values ('h4', '', 1);

insert into board (position, piece, room_number)
values ('a5', '', 1);
insert into board (position, piece, room_number)
values ('b5', '', 1);
insert into board (position, piece, room_number)
values ('c5', '', 1);
insert into board (position, piece, room_number)
values ('d5', '', 1);
insert into board (position, piece, room_number)
values ('e5', '', 1);
insert into board (position, piece, room_number)
values ('f5', '', 1);
insert into board (position, piece, room_number)
values ('g5', '', 1);
insert into board (position, piece, room_number)
values ('h5', '', 1);

insert into board (position, piece, room_number)
values ('a6', '', 1);
insert into board (position, piece, room_number)
values ('b6', '', 1);
insert into board (position, piece, room_number)
values ('c6', '', 1);
insert into board (position, piece, room_number)
values ('d6', '', 1);
insert into board (position, piece, room_number)
values ('e6', '', 1);
insert into board (position, piece, room_number)
values ('f6', '', 1);
insert into board (position, piece, room_number)
values ('g6', '', 1);
insert into board (position, piece, room_number)
values ('h6', '', 1);