create table room
(
    id int auto_increment,
    name varchar(20) not null,
    password varchar(20) not null,
    primary key(id)
);

create table piece
(
    roomnumber int not null,
    position varchar(2) not null,
    team     varchar(5) not null,
    name     varchar(6) not null,
    foreign key (roomnumber) references room(id),
    primary key (roomnumber, position)
);

create table game
(
    roomnumber int not null,
    state varchar(7),
    turn  varchar(5),
    foreign key (roomnumber) references room(id),
    primary key (roomnumber)
);