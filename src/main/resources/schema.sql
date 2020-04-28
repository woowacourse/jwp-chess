create table room(
    id uuid primary key,
    name varchar(255) not null,
    password varchar(20) not null
);

create table game(
    room uuid not null,
    turn varchar(10) not null
);

create table piece(
    game uuid not null,
    name varchar(20) not null,
    color varchar(20) not null,
    position varchar(20) not null
);