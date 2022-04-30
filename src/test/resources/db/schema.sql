DROP TABLE IF EXISTS game;
DROP TABLE IF EXISTS square;
DROP TABLE IF EXISTS state;

create table game (
    id int not null AUTO_INCREMENT PRIMARY KEY,
    title varchar(50) not null,
    password varchar(20) not null
);

CREATE TABLE state
(
  chess_id int not null,
  name VARCHAR(20) NOT NULL
);

CREATE TABLE square
(
  chess_id int not null,
  position VARCHAR(2) NOT NULL,
  team VARCHAR(10) NOT NULL,
  symbol VARCHAR(10) NOT NULL
);

insert into game (title, password) values ('제목', 'password');
insert into square (chess_id, position, team, symbol) values (1, 'a1', 'BLACK', 'PAWN');
insert into state(chess_id, name) values ('1', 'BLACK_TURN');
