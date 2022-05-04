drop table piece if exists;
create table piece
(
    pieceId   int(10) not null AUTO_INCREMENT,
    roomId    int(10) not null,
    name      varchar(10) not null,
    position  varchar(10) not null,
    teamColor varchar(10) not null,
    primary key (pieceId)
);
