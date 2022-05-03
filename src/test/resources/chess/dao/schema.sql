drop table pieces if exists;
drop table rooms if exists;

create table rooms
(
    id       bigint       not null auto_increment,
    name     varchar(10)  not null,
    password varchar(100) not null,
    status   varchar(10)  not null,
    turn     varchar(10)  not null,
    primary key (id)
);

create table pieces
(
    id       bigint      not null auto_increment,
    room_id  bigint      not null,
    position varchar(10) not null,
    type     varchar(10) not null,
    color    varchar(10) not null,
    primary key (id),
    foreign key (room_id) references rooms (id) on delete cascade
);
