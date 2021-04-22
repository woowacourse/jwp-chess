create table if not exists move
(
    id    int        not null auto_increment,
    start varchar(2) not null,
    end   varchar(2) not null,
    primary key (id)
);

create table if not exists room
(
    id int not null auto_increment,
    name varchar(50) not null,
    primary key (id)
);