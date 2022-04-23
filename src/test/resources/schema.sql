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

-- TODO: replace legacy game table

CREATE TABLE game2
(
    id    BIGINT NOT NULL AUTO_INCREMENT,
    name varchar(20) NOT NULL,
    password VARCHAR(100) NOT NULL,
    running BOOLEAN NOT NULL DEFAULT true,
    PRIMARY KEY (id),
    UNIQUE (name)
);

INSERT INTO game2 (id, name, password, running)
VALUES (1, '진행중인_게임', 'encrypted1', true),
       (2, '종료된_게임', 'encrypted2', false),
       (3, '이미_존재하는_게임명', 'encrypted3', true);
