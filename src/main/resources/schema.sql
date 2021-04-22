CREATE TABLE IF NOT EXISTS chessroom
(
    room_id       INT          NOT NULL AUTO_INCREMENT,
    room_name     VARCHAR(255) NOT NULL,
    room_password VARCHAR(255) NOT NULL,
    PRIMARY KEY (room_id)
);

CREATE TABLE IF NOT EXISTS chessgame
(
    command_log  INT         NOT NULL AUTO_INCREMENT,
    room_id      INT         NOT NULL,
    target       VARCHAR(12) NOT NULL,
    destination  VARCHAR(12) NOT NULL,
    command_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (command_log)
);