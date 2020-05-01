create database wootecochess DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

create table room DEFAULT CHARSET=utf8 COLLATE utf8_general_ci
(
    id bigint auto_increment primary key,
    name varchar(64) not null,
    unique(name)
);

create table game DEFAULT CHARSET=utf8 COLLATE utf8_general_ci
(
    room bigint,
    turn int not null
);

create table cell DEFAULT CHARSET=utf8 COLLATE utf8_general_ci
(
    game bigint not null,
    position varchar(64) not null,
    piece varchar(64) not null
)