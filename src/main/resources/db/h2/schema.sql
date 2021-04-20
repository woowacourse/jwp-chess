DROP TABLE game IF EXISTS;
DROP TABLE score IF EXISTS;
DROP TABLE state IF EXISTS;
DROP TABLE piece IF EXISTS;
DROP TABLE history IF EXISTS;

-- -----------------------------------------------------
-- Table `game`
-- -----------------------------------------------------
CREATE TABLE game
(
    id             INTEGER IDENTITY PRIMARY KEY,
    room_name      VARCHAR(100) NOT NULL,
    white_username VARCHAR(45)  NOT NULL,
    black_username VARCHAR(45)  NOT NULL
);
CREATE
INDEX game_id ON game (id);

-- -----------------------------------------------------
-- Table `history`
-- -----------------------------------------------------
CREATE TABLE history
(
    id           INTEGER IDENTITY PRIMARY KEY,
    game_id      INTEGER     NOT NULL,
    move_command VARCHAR(45) NOT NULL,
    turn_owner   VARCHAR(45) NOT NULL,
    turn_number  VARCHAR(45) NOT NULL,
    playing      BIT         NOT NULL
);
CREATE
INDEX history_id ON history (id);
ALTER TABLE history
    ADD CONSTRAINT fk_game_history FOREIGN KEY (game_id) REFERENCES game (id);

-- -----------------------------------------------------
-- Table `piece`
-- -----------------------------------------------------
CREATE TABLE piece
(
    id       INTEGER IDENTITY PRIMARY KEY,
    game_id  INTEGER     NOT NULL,
    position VARCHAR(45) NOT NULL,
    symbol   VARCHAR(45) NOT NULL
);
CREATE
INDEX piece_id ON piece (id);
ALTER TABLE piece
    ADD CONSTRAINT fk_game_piece FOREIGN KEY (game_id) REFERENCES game (id);

-- -----------------------------------------------------
-- Table `score`
-- -----------------------------------------------------
CREATE TABLE score
(
    id          INTEGER IDENTITY PRIMARY KEY,
    game_id     INTEGER NOT NULL,
    white_score DOUBLE  NOT NULL,
    black_score DOUBLE  NOT NULL
);
CREATE
INDEX score_id ON score (id);
ALTER TABLE score
    ADD CONSTRAINT fk_game_score FOREIGN KEY (game_id) REFERENCES game (id);


-- -----------------------------------------------------
-- Table `state`
-- -----------------------------------------------------
CREATE TABLE state
(
    id          INTEGER IDENTITY PRIMARY KEY,
    game_id     INTEGER     NOT NULL,
    turn_owner  VARCHAR(45) NOT NULL,
    turn_number INTEGER     NOT NULL,
    playing     BIT         NOT NULL
);
CREATE
INDEX state_id ON state (id);
ALTER TABLE state
    ADD CONSTRAINT fk_game_state FOREIGN KEY (game_id) REFERENCES game (id);
