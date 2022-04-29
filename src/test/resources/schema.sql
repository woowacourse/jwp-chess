DROP TABLE game IF EXISTS;
DROP TABLE event IF EXISTS;

CREATE TABLE game
(
    id    BIGINT NOT NULL AUTO_INCREMENT,
    name varchar(20) NOT NULL,
    password VARCHAR(100) NOT NULL,
    opponent_password VARCHAR(100),
    running BOOLEAN NOT NULL DEFAULT true,
    PRIMARY KEY (id),
    UNIQUE (name)
);

CREATE TABLE event(
    game_id     BIGINT      NOT NULL,
    type        VARCHAR(20) NOT NULL,
    description VARCHAR(20)
);

INSERT INTO game (id, name, password, opponent_password, running)
VALUES (1, '진행중인_게임', 'encrypted1', 'enemy1', true),
       (2, '종료된_게임', 'encrypted2', 'enemy2', false),
       (3, '이미_존재하는_게임명', 'encrypted3', null, true),
       (4, '참여자가_있는_게임', 'encrypted4', 'enemy4', true),
       (5, '참여자가_없는_게임', 'encrypted5', null, true);

INSERT INTO event (game_id, type, description)
VALUES (1, 'MOVE', 'a2 a4'), (1, 'MOVE', 'a7 a5'), (2, 'MOVE', 'a2 a3');
