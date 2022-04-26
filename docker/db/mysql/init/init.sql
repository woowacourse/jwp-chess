create table room
(
    id int(10) NOT NULL AUTO_INCREMENT,
    title varchar(255) NOT NULL,
    color varchar(5) NOT NULL,
    primary key (id)
);

create table board
(
    board_id int(10) NOT NULL AUTO_INCREMENT,
    position varchar(2) NOT NULL,
    piece varchar(10) NOT NULL,
    primary key (board_id),
    foreign key (board_id) references room (id)
);


--create table board
--(
--    board_id int(10) NOT NULL AUTO_INCREMENT,
--    position varchar(2) NOT NULL,
--    piece varchar(10) NOT NULL,
--    primary key (board_id)
--    foreign key 'board_id' references 'room' ('id')
--);
