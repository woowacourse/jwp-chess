create table board(
	id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    position varchar(10) NOT NULL,
    pieceName varchar(10) NOT NULL
);

create table turn(
	id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    teamName varchar(10) NOT NULL
);