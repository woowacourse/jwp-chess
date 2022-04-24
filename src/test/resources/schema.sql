DROP TABLE game IF EXISTS;
DROP TABLE event IF EXISTS;

CREATE TABLE game(
    id      BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    running BOOLEAN NOT NULL DEFAULT true
);

CREATE TABLE event(
    game_id     BIGINT      NOT NULL,
    type        VARCHAR(20) NOT NULL,
    description VARCHAR(20)
);

INSERT INTO game (id, running)
VALUES (1, true), (2, false);

INSERT INTO event (game_id, type, description)
VALUES (1, 'MOVE', 'a2 a4'), (1, 'MOVE', 'a7 a5'), (2, 'MOVE', 'a2 a3');