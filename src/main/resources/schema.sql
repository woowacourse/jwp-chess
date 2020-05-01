CREATE TABLE IF NOT EXISTS chess_game_entity
(
    id    INT        NOT NULL AUTO_INCREMENT,
    state VARCHAR(8) NOT NULL,
    board VARCHAR(64),
    turn  VARCHAR(5),
    PRIMARY KEY (id)
);