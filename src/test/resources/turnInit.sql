drop table turn if exists;
CREATE TABLE turn
(
    roomId int        not null primary key,
    team   varchar(5) not null
);