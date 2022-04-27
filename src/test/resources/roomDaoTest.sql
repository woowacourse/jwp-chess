drop table room if exists;

create table room
(
    roomId    int(10) not null AUTO_INCREMENT,
    name      varchar(10) not null,
    password  varchar(10) not null,
    gameState varchar(7)  not null,
    turn      varchar(5)  not null,
    primary key (roomId)
);
