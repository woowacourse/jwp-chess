CREATE TABLE game
(
    game_id       int NOT NULL AUTO_INCREMENT,
    current_turn  varchar(10) DEFAULT 'white',
    game_title    varchar(20) DEFAULT NULL,
    game_password varchar(20) DEFAULT NULL,
    PRIMARY KEY (game_id)
);

CREATE TABLE piece
(
    piece_id    int         NOT NULL AUTO_INCREMENT,
    game_id     int         NOT NULL,
    piece_name  varchar(10) NOT NULL,
    piece_color varchar(10) NOT NULL,
    position    varchar(2)  NOT NULL,
    PRIMARY KEY (piece_id),
    FOREIGN KEY (game_id)
        REFERENCES game (game_id)
);
