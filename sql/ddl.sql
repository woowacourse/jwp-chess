create table room_status
(
    id int auto_increment,
    room_name char(10) null,
    constraint room_status_pk
        primary key (id)
);



create table game_status
(
    id int auto_increment,
    room_id int null,
    turn char(10) null,
    board text null,
    constraint game_status_pk
        primary key (id)
);