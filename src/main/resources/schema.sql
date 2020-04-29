create table if not exists game(
    id bigint auto_increment,
    state varchar(255) not null,
    turn varchar(255) not null,
    board varchar(255) not null,
    primary key(id)
);