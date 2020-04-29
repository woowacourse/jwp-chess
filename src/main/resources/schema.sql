create table if not exists chess_recode(
    id bigint auto_increment,
    state varchar(8) not null,
    board varchar(64) not null,
    turn varchar(5) not null,
    primary key(id)
);