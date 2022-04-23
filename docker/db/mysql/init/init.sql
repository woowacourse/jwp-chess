DROP TABLE IF EXISTS board;
DROP TABLE IF EXISTS piece;

CREATE TABLE board
(
    id   INT(10) not null AUTO_INCREMENT ,
    turn VARCHAR (5) not null,
    primary key (id)
);

INSERT INTO board (turn) VALUES ("white");

CREATE TABLE piece
(
    id       INT(10) not null AUTO_INCREMENT,
    board_id INT(10),
    position CHAR(2),
    type     VARCHAR (20) not null,
    team     VARCHAR (10) not null,
    foreign key (board_id) references board (id) ON DELETE CASCADE ,
    primary key (id, board_id, position)
);
