drop table room if exists;
CREATE TABLE room
(
    id       int         not null auto_increment primary key,
    title    varchar(30) not null,
    password varchar(8)  not null
);