create table if not exists room
(
    id bigint auto_increment primary key,
    name varchar(64) not null,
    unique(name)
);

create table if not exists game
(
    room bigint,
    turn int not null
);

create table if not exists cell
(
    game bigint not null,
    position varchar(64) not null,
    piece varchar(64) not null
);