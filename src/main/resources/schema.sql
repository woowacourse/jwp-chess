DROP TABLE IF EXISTS board;
DROP TABLE IF EXISTS turn;
DROP TABLE IF EXISTS room;

create table room(
    room_name varchar(20) not null,
    primary key (room_name)
);

create table board
(
    position varchar(12) not null,
    piece    varchar(12) not null,
    room_name varchar(20) not null,
    primary key (position),
    foreign key (room_name) references room (room_name) on update cascade
);

insert into room(room_name) values ('whatsup');

insert into board (position, piece, room_name)
values ('a1', '&#9814;', 'whatsup');
insert into board (position, piece, room_name)
values ('b1', '&#9816;', 'whatsup');
insert into board (position, piece, room_name)
values ('c1', '&#9815;', 'whatsup');
insert into board (position, piece, room_name)
values ('d1', '&#9812;', 'whatsup');
insert into board (position, piece, room_name)
values ('e1', '&#9813;', 'whatsup');
insert into board (position, piece, room_name)
values ('f1', '&#9815;', 'whatsup');
insert into board (position, piece, room_name)
values ('g1', '&#9816;', 'whatsup');
insert into board (position, piece, room_name)
values ('h1', '&#9814;', 'whatsup');

insert into board (position, piece, room_name)
values ('a8', '&#9820;', 'whatsup');
insert into board (position, piece, room_name)
values ('b8', '&#9822;', 'whatsup');
insert into board (position, piece, room_name)
values ('c8', '&#9821;', 'whatsup');
insert into board (position, piece, room_name)
values ('d8', '&#9818;', 'whatsup');
insert into board (position, piece, room_name)
values ('e8', '&#9819;', 'whatsup');
insert into board (position, piece, room_name)
values ('f8', '&#9821;', 'whatsup');
insert into board (position, piece, room_name)
values ('g8', '&#9822;', 'whatsup');
insert into board (position, piece, room_name)
values ('h8', '&#9820;', 'whatsup');

insert into board (position, piece, room_name)
values ('a7', '&#9823;', 'whatsup');
insert into board (position, piece, room_name)
values ('b7', '&#9823;', 'whatsup');
insert into board (position, piece, room_name)
values ('c7', '&#9823;', 'whatsup');
insert into board (position, piece, room_name)
values ('d7', '&#9823;', 'whatsup');
insert into board (position, piece, room_name)
values ('e7', '&#9823;', 'whatsup');
insert into board (position, piece, room_name)
values ('f7', '&#9823;', 'whatsup');
insert into board (position, piece, room_name)
values ('g7', '&#9823;', 'whatsup');
insert into board (position, piece, room_name)
values ('h7', '&#9823;', 'whatsup');

insert into board (position, piece, room_name)
values ('a2', '&#9817;', 'whatsup');
insert into board (position, piece, room_name)
values ('b2', '&#9817;', 'whatsup');
insert into board (position, piece, room_name)
values ('c2', '&#9817;', 'whatsup');
insert into board (position, piece, room_name)
values ('d2', '&#9817;', 'whatsup');
insert into board (position, piece, room_name)
values ('e2', '&#9817;', 'whatsup');
insert into board (position, piece, room_name)
values ('f2', '&#9817;', 'whatsup');
insert into board (position, piece, room_name)
values ('g2', '&#9817;', 'whatsup');
insert into board (position, piece, room_name)
values ('h2', '&#9817;', 'whatsup');

insert into board (position, piece, room_name)
values ('a3', '', 'whatsup');
insert into board (position, piece, room_name)
values ('b3', '', 'whatsup');
insert into board (position, piece, room_name)
values ('c3', '', 'whatsup');
insert into board (position, piece, room_name)
values ('d3', '', 'whatsup');
insert into board (position, piece, room_name)
values ('e3', '', 'whatsup');
insert into board (position, piece, room_name)
values ('f3', '', 'whatsup');
insert into board (position, piece, room_name)
values ('g3', '', 'whatsup');
insert into board (position, piece, room_name)
values ('h3', '', 'whatsup');

insert into board (position, piece, room_name)
values ('a4', '', 'whatsup');
insert into board (position, piece, room_name)
values ('b4', '', 'whatsup');
insert into board (position, piece, room_name)
values ('c4', '', 'whatsup');
insert into board (position, piece, room_name)
values ('d4', '', 'whatsup');
insert into board (position, piece, room_name)
values ('e4', '', 'whatsup');
insert into board (position, piece, room_name)
values ('f4', '', 'whatsup');
insert into board (position, piece, room_name)
values ('g4', '', 'whatsup');
insert into board (position, piece, room_name)
values ('h4', '', 'whatsup');

insert into board (position, piece, room_name)
values ('a5', '', 'whatsup');
insert into board (position, piece, room_name)
values ('b5', '', 'whatsup');
insert into board (position, piece, room_name)
values ('c5', '', 'whatsup');
insert into board (position, piece, room_name)
values ('d5', '', 'whatsup');
insert into board (position, piece, room_name)
values ('e5', '', 'whatsup');
insert into board (position, piece, room_name)
values ('f5', '', 'whatsup');
insert into board (position, piece, room_name)
values ('g5', '', 'whatsup');
insert into board (position, piece, room_name)
values ('h5', '', 'whatsup');

insert into board (position, piece, room_name)
values ('a6', '', 'whatsup');
insert into board (position, piece, room_name)
values ('b6', '', 'whatsup');
insert into board (position, piece, room_name)
values ('c6', '', 'whatsup');
insert into board (position, piece, room_name)
values ('d6', '', 'whatsup');
insert into board (position, piece, room_name)
values ('e6', '', 'whatsup');
insert into board (position, piece, room_name)
values ('f6', '', 'whatsup');
insert into board (position, piece, room_name)
values ('g6', '', 'whatsup');
insert into board (position, piece, room_name)
values ('h6', '', 'whatsup');

create table turn
(
    turn_owner varchar(10) not null,
    room_name varchar(20) not null,
    primary key (turn_owner),
    foreign key (room_name) references room (room_name) on update cascade
);

insert into turn (turn_owner, room_name)
values ('white', 'whatsup');