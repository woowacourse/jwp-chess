CREATE TABLE turn (
    team varchar(5) not null primary key
);

insert into turn (team) values ('WHITE');

CREATE TABLE piece (
    position varchar(3) not null primary key,
    name varchar(2) not null,
    team varchar(5) not null
);
