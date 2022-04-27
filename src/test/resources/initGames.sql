DROP TABLE GAMES IF EXISTS;

CREATE TABLE games
(
    game_id     int         not null AUTO_INCREMENT,
    password    varchar(25) not null,
    turn        varchar(5)  not null,
    primary key (game_id)
);