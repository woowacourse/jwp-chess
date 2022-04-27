DROP TABLE IF EXISTS piece;
DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS board;

CREATE TABLE board
(
    id   INT(10)    not null AUTO_INCREMENT,
    turn VARCHAR(5) not null,
    primary key (id)
);

CREATE TABLE room
(
    id       INT(10)     not null AUTO_INCREMENT,
    board_id INT(10),
    title    VARCHAR(30) not null,
    password VARCHAR(30) not null,
    foreign key (board_id) references board (id) ON DELETE CASCADE,
    primary key (id, board_id)
);

CREATE TABLE piece
(
    id       INT(10)     not null AUTO_INCREMENT,
    board_id INT(10),
    position CHAR(2),
    type     VARCHAR(20) not null,
    team     VARCHAR(10) not null,
    foreign key (board_id) references board (id) ON DELETE CASCADE,
    primary key (id, board_id, position)
);

