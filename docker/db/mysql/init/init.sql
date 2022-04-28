create table board(
    location varchar(20) not null,
    name varchar(20) not null,
    color varchar(20) not null,
    roomID int not null,
    foreign key (roomID) references room(id) on delete cascade,
    primary key (roomID, location)
);

create table state(
    roomID int not null,
    now varchar(20) not null,
    color varchar(20),
    foreign key (roomID) references room(id) on delete cascade,
    primary key (roomID)
);

create table room(
    id int not null auto_increment,
    password varchar(50) not null,
    name varchar(50) not null,
    primary key(id)
);