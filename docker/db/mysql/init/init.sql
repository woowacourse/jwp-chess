CREATE TABLE game
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(30) NOT NULL,
    password VARCHAR(30) NOT NULL,
    running BOOLEAN NOT NULL DEFAULT true,
    PRIMARY KEY (id)
);

CREATE TABLE event
(
    game_id BIGINT NOT NULL,
    type VARCHAR(20) NOT NULL,
    description VARCHAR(20)
);
