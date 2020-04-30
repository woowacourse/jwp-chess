create table room(
        id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
        name varchar(30) NOT NULL
);

create table board(
        id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
        room int NOT NULL,
        position varchar(10) NOT NULL,
        pieceName varchar(10) NOT NULL
  );

create table turn(
        id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
        room int NOT NULL,
        teamName varchar(10) NOT NULL
);