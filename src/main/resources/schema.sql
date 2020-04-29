DROP TABLE IF EXISTS PIECE;
DROP TABLE IF EXISTS GAME;

CREATE TABLE GAME
(
    id   bigint auto_increment,
    turn varchar(255),
    primary key (id)
);

CREATE TABLE PIECE
(
    id       bigint auto_increment,
    game_id  bigint,
    game_key  bigint,
    symbol   varchar(255),
    position varchar(255),
    team     varchar(255),
    primary key (id)
);

ALTER TABLE PIECE
    ADD FOREIGN KEY (game_id)
        REFERENCES GAME (id);