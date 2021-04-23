CREATE TABLE room
(
    room_id   SERIAL,
    room_name VARCHAR(30) NOT NULL,
    PRIMARY KEY (room_id)
);

CREATE TABLE command
(
    command_id SERIAL,
    room_id    BIGINT UNSIGNED,
    move_from  VARCHAR(30),
    move_to    VARCHAR(30),
    PRIMARY KEY (command_id),
    FOREIGN KEY (room_id) REFERENCES room (room_id) ON DELETE CASCADE
);

CREATE TABLE player
(
    player_id   INT,
    player_name VARCHAR(30),
    win_count   INT DEFAULT 0,
    lose_count  INT DEFAULT 0,
    PRIMARY KEY (player_id)
);

CREATE TABLE game
(
    game_id     INT,
    game_name   VARCHAR(30),
    player_a_id INT,
    player_b_id INT,
    finished    BOOLEAN,
    winner_id   INT,
    PRIMARY KEY (game_id),
    FOREIGN KEY (player_a_id, player_b_id) REFERENCES player (player_id)
);
