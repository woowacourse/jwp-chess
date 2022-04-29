CREATE TABLE game
(
    id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name     varchar(255) NOT NULL UNIQUE,
    password varchar(255) NOT NULL,
    salt     varchar(255) NOT NULL,
    state    varchar(20)  NOT NULL
);

CREATE TABLE piece
(
    id       BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    game_id  BIGINT      NOT NULL,
    position VARCHAR(10) NOT NULL,
    type     VARCHAR(10) NOT NULL,
    color    VARCHAR(10) NOT NULL,
    FOREIGN KEY (game_id)
        REFERENCES game (id) ON DELETE CASCADE
);
