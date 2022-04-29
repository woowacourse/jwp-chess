DROP TABLE room IF EXISTS;

create table room
(
    id   int(10)     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(20) not null,
    pw   varchar(20) not null,
    primary key (id)
);

DROP TABLE board IF EXISTS;

create table board
(
    id       int(10)     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    position varchar(10) not null,
    piece    varchar(20) not null,
    roomId   INT,
    primary key (id),
    FOREIGN KEY (roomId) REFERENCES room (id) ON DELETE CASCADE
);

DROP TABLE game_status IF EXISTS;

create table game_status
(
    id     int(10)     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    status varchar(10) not null,
    roomId INT,
    primary key (id),
    FOREIGN KEY (roomId) REFERENCES room (id) ON DELETE CASCADE
);

DROP TABLE turn IF EXISTS;

create table turn
(
    id     int(10)     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    team   varchar(10) not null,
    roomId INT,
    primary key (id),
    FOREIGN KEY (roomId) REFERENCES room (id) ON DELETE CASCADE
);





