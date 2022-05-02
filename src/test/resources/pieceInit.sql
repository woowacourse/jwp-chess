drop table piece if exists;
CREATE TABLE piece
(
    roomId   int        not null,
    position varchar(3) not null,
    name     varchar(2) not null,
    team     varchar(5) not null,
    primary key (roomId, position)
);