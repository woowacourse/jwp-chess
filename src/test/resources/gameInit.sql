drop table game if exists;

CREATE TABLE game
(
    roomId int not null primary key,
    state  varchar(3)
);