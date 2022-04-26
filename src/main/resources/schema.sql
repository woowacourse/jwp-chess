create table board
(
    position varchar(10) not null,
    piece    varchar(20) not null,
    primary key (position)
);

create table game_status
(
    status varchar(10) not null,
    primary key (status)
);

create table turn
(
    team varchar(10) not null,
    primary key (team)
);
