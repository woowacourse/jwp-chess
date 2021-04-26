CREATE TABLE IF NOT EXISTS game
(
    game_id INTEGER NOT NULL AUTO_INCREMENT,
    is_end  BOOLEAN NOT NULL DEFAULT false,
    PRIMARY KEY (game_id)
);


CREATE TABLE IF NOT EXISTS room
(
    room_id INTEGER     NOT NULL AUTO_INCREMENT,
    name    VARCHAR(21) NOT NULL,
    pw      VARCHAR(21) NOT NULL,
    game_id INTEGER     NOT NULL,
    PRIMARY KEY (room_id),
    FOREIGN KEY (game_id) REFERENCES game (game_id)
);


CREATE TABLE IF NOT EXISTS team
(
    team_id INTEGER     NOT NULL AUTO_INCREMENT,
    name    VARCHAR(21) NOT NULL,
    is_turn BOOLEAN     NOT NULL DEFAULT false,
    game_id INTEGER     NOT NULL,
    PRIMARY KEY (team_id),
    FOREIGN KEY (game_id) REFERENCES game (game_id)
);

CREATE TABLE IF NOT EXISTS piece
(
    piece_id INTEGER     NOT NULL AUTO_INCREMENT,
    name     VARCHAR(21) NOT NULL,
    color    VARCHAR(21) NOT NULL,
    position VARCHAR(10) NOT NULL,
    game_id  INTEGER     NOT NULL,
    PRIMARY KEY (piece_id),
    FOREIGN KEY (game_id) REFERENCES game (game_id)
);