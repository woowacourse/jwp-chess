DROP TABLE room IF EXISTS;
DROP TABLE user IF EXISTS;
DROP TABLE game IF EXISTS;
DROP TABLE piece IF EXISTS;
DROP TABLE history IF EXISTS;

-- -----------------------------------------------------
-- Table `game`
-- -----------------------------------------------------
CREATE TABLE room
(
    id             INTEGER IDENTITY PRIMARY KEY,
    game_id        INTEGER,
    room_name      VARCHAR(100) NOT NULL,
    white_user_id   INTEGER NOT NULL,
    black_user_id   INTEGER
);
CREATE
INDEX game_id ON game (id);
ALTER TABLE room
    ADD CONSTRAINT fk_game_room FOREIGN KEY (game_id) REFERENCES game (id);

-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------
CREATE TABLE user
(
    id             INTEGER IDENTITY PRIMARY KEY,
    username VARCHAR(45)  NOT NULL,
    password VARCHAR(15) NOT NULL,
);
CREATE
INDEX user_id ON user (id);

-- -----------------------------------------------------
-- Table `game`
-- -----------------------------------------------------
CREATE TABLE game
(
    id           INTEGER IDENTITY PRIMARY KEY,
    turn_owner  VARCHAR(45) NOT NULL,
    turn_number INTEGER     NOT NULL,
    playing     BIT         NOT NULL,
    white_score DOUBLE  NOT NULL,
    black_score DOUBLE  NOT NULL
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