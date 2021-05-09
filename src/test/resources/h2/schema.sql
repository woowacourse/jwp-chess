DROP TABLE room IF EXISTS;
DROP TABLE user IF EXISTS;
DROP TABLE game IF EXISTS;
DROP TABLE square IF EXISTS;
DROP TABLE history IF EXISTS;

-- -----------------------------------------------------
-- Table `room`
-- -----------------------------------------------------
CREATE TABLE room
(
    id            INTEGER IDENTITY PRIMARY KEY,
    game_id       INTEGER,
    name          VARCHAR(10) NOT NULL,
    white_user_id INTEGER     NOT NULL,
    black_user_id INTEGER
);
CREATE
    INDEX room_id ON room (id);

-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------
CREATE TABLE user
(
    id       INTEGER IDENTITY PRIMARY KEY,
    name     VARCHAR(45) NOT NULL,
    password VARCHAR(15) NOT NULL
);
CREATE
    INDEX user_id ON user (id);

-- -----------------------------------------------------
-- Table `game`
-- -----------------------------------------------------
CREATE TABLE game
(
    id          INTEGER IDENTITY PRIMARY KEY,
    turn_owner  VARCHAR(45) NOT NULL,
    turn_number INTEGER     NOT NULL,
    playing     BIT         NOT NULL,
    white_score DOUBLE      NOT NULL,
    black_score DOUBLE      NOT NULL
);
CREATE
    INDEX game_id ON game (id);

-- -----------------------------------------------------
-- Table `history`
-- -----------------------------------------------------
CREATE TABLE history
(
    id          INTEGER IDENTITY PRIMARY KEY,
    game_id     INTEGER     NOT NULL,
    source      VARCHAR(45) NOT NULL,
    target      VARCHAR(45) NOT NULL,
    turn_owner  VARCHAR(45) NOT NULL,
    turn_number VARCHAR(45) NOT NULL,
    playing     BIT         NOT NULL
);
CREATE
    INDEX history_id ON history (id);
ALTER TABLE history
    ADD CONSTRAINT fk_game_history FOREIGN KEY (game_id) REFERENCES game (id);


-- -----------------------------------------------------
-- Table `square`
-- -----------------------------------------------------
CREATE TABLE square
(
    id       INTEGER IDENTITY PRIMARY KEY,
    game_id  INTEGER     NOT NULL,
    position VARCHAR(45) NOT NULL,
    symbol   VARCHAR(45) NOT NULL
);
CREATE
    INDEX square_id ON square (id);
ALTER TABLE square
    ADD CONSTRAINT fk_game_square FOREIGN KEY (game_id) REFERENCES game (id);