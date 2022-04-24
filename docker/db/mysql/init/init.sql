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
