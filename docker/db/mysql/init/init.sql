CREATE TABLE game
(
    id    BIGINT NOT NULL AUTO_INCREMENT,
    running BOOLEAN NOT NULL DEFAULT true,
    PRIMARY KEY (id)
);

CREATE TABLE event
(
    game_id  BIGINT NOT NULL,
    type  VARCHAR(20) NOT NULL,
    description VARCHAR(20)
);

-- TODO: replace legacy game table

CREATE TABLE game2
(
    id    BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    password VARCHAR(100) NOT NULL,
    running BOOLEAN NOT NULL DEFAULT true,
    PRIMARY KEY (id),
    UNIQUE (name)
);
