CREATE TABLE room (
    id int not null auto_increment primary key,
    title varchar(30) not null,
    password varchar(8) not null
);

CREATE TABLE turn (
    roomId int not null primary key,
    team varchar(5) not null,
    foreign key(roomId) REFERENCES room (id)
    on delete cascade
);

-- insert into turn (team) values ('WHITE'); <- 첫 시작에 넣어주기

CREATE TABLE piece (
    roomId int not null,
    position varchar(3) not null,
    name varchar(2) not null,
    team varchar(5) not null,
    primary key (roomId, position),
    foreign key(roomId) REFERENCES room (id)
    on delete cascade
);
