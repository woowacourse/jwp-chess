CREATE TABLE room
(
    id       bigint      not null auto_increment primary key,
    name     varchar(30) not null,
    password varchar(20) not null
);

CREATE TABLE turn
(
    id     bigint     not null auto_increment primary key,
    roomId bigint     not null,
    team   varchar(5) not null,
    constraint fk_roomId foreign key (roomId) references room (id) on delete cascade
);

CREATE TABLE piece
(
    id       bigint     not null auto_increment primary key,
    roomId   bigint     not null,
    position varchar(3) not null,
    name     varchar(2) not null,
    team     varchar(5) not null,
    constraint roomId foreign key (roomId) references room (id) on delete cascade
);