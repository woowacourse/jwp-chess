DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS piece;

CREATE TABLE room
(
    id   INT(10) not null AUTO_INCREMENT ,
    turn VARCHAR (5) not null,
    title VARCHAR (10) not null,
    password VARCHAR (10) not null,
    primary key (id)
);

CREATE TABLE piece
(
    id       INT(10) not null AUTO_INCREMENT,
    room_id INT(10),
    position CHAR(2),
    type     VARCHAR (20) not null,
    team     VARCHAR (10) not null,
    foreign key (room_id) references room (id) ON DELETE CASCADE ,
    primary key (id)
);

