DROP TABLE game IF EXISTS;
DROP TABLE event IF EXISTS;

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

INSERT INTO game (id, title, password, running)
VALUES (1, 'title1', '1234', true), (2, 'title2', '5678', false);

INSERT INTO event (game_id, type, description)
VALUES (1, 'MOVE', 'a2 a4'), (1, 'MOVE', 'a7 a5'), (2, 'MOVE', 'a2 a3');