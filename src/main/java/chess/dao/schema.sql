CREATE TABLE game
(
    game_id   SERIAL,
    game_name VARCHAR(30) NOT NULL,
    PRIMARY KEY (game_id)
);

CREATE TABLE command
(
    command_id SERIAL,
    game_id    BIGINT UNSIGNED,
    move_from  VARCHAR(30),
    move_to    VARCHAR(30),
    PRIMARY KEY (command_id),
    FOREIGN KEY (game_id) REFERENCES game (game_id)  ON DELETE CASCADE
);

