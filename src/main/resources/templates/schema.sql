drop table board;
create table board (
                       position varchar(12) not null,
                       piece varchar(12) not null,
                       primary key (position) );
insert into board (position, piece) values ('a1', '&#9814;');
insert into board (position, piece) values ('b1', '&#9816;');
insert into board (position, piece) values ('c1', '&#9815;');
insert into board (position, piece) values ('d1', '&#9812;');
insert into board (position, piece) values ('e1', '&#9813;');
insert into board (position, piece) values ('f1', '&#9815;');
insert into board (position, piece) values ('g1', '&#9816;');
insert into board (position, piece) values ('h1', '&#9814;');

insert into board (position, piece) values ('a8', '&#9820;');
insert into board (position, piece) values ('b8', '&#9822;');
insert into board (position, piece) values ('c8', '&#9821;');
insert into board (position, piece) values ('d8', '&#9818;');
insert into board (position, piece) values ('e8', '&#9819;');
insert into board (position, piece) values ('f8', '&#9821;');
insert into board (position, piece) values ('g8', '&#9822;');
insert into board (position, piece) values ('h8', '&#9820;');

insert into board (position, piece) values('a7', '&#9823;');
insert into board (position, piece) values('b7', '&#9823;');
insert into board (position, piece) values('c7', '&#9823;');
insert into board (position, piece) values('d7', '&#9823;');
insert into board (position, piece) values('e7', '&#9823;');
insert into board (position, piece) values('f7', '&#9823;');
insert into board (position, piece) values('g7', '&#9823;');
insert into board (position, piece) values('h7', '&#9823;');

insert into board (position, piece) values ('a2', '&#9817;');
insert into board (position, piece) values ('b2', '&#9817;');
insert into board (position, piece) values ('c2', '&#9817;');
insert into board (position, piece) values ('d2', '&#9817;');
insert into board (position, piece) values ('e2', '&#9817;');
insert into board (position, piece) values ('f2', '&#9817;');
insert into board (position, piece) values ('g2', '&#9817;');
insert into board (position, piece) values ('h2', '&#9817;');

insert into board (position, piece) values ('a3', '');
insert into board (position, piece) values ('b3', '');
insert into board (position, piece) values ('c3', '');
insert into board (position, piece) values ('d3', '');
insert into board (position, piece) values ('e3', '');
insert into board (position, piece) values ('f3', '');
insert into board (position, piece) values ('g3', '');
insert into board (position, piece) values ('h3', '');

insert into board (position, piece) values ('a4', '');
insert into board (position, piece) values ('b4', '');
insert into board (position, piece) values ('c4', '');
insert into board (position, piece) values ('d4', '');
insert into board (position, piece) values ('e4', '');
insert into board (position, piece) values ('f4', '');
insert into board (position, piece) values ('g4', '');
insert into board (position, piece) values ('h4', '');

insert into board (position, piece) values ('a5', '');
insert into board (position, piece) values ('b5', '');
insert into board (position, piece) values ('c5', '');
insert into board (position, piece) values ('d5', '');
insert into board (position, piece) values ('e5', '');
insert into board (position, piece) values ('f5', '');
insert into board (position, piece) values ('g5', '');
insert into board (position, piece) values ('h5', '');

insert into board (position, piece) values ('a6', '');
insert into board (position, piece) values ('b6', '');
insert into board (position, piece) values ('c6', '');
insert into board (position, piece) values ('d6', '');
insert into board (position, piece) values ('e6', '');
insert into board (position, piece) values ('f6', '');
insert into board (position, piece) values ('g6', '');
insert into board (position, piece) values ('h6', '');

drop table turn;
create table turn (
                      turn_owner varchar(10) not null,
                      primary key (turn_owner));

insert into turn (turn_owner) values ('white');