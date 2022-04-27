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
