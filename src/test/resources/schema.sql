CREATE TABLE IF NOT EXISTS rooms(
    id          INT(11) NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    state       VARCHAR(45)  NOT NULL,
    currentteam VARCHAR(45)  NOT NULL,
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS pieces(
    id        INT(11) NOT NULL AUTO_INCREMENT,
    roomid    INT(11) NOT NULL,
    signature VARCHAR(45) NOT NULL,
    team      VARCHAR(45) NOT NULL,
    location  VARCHAR(45) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (roomid) REFERENCES rooms (id)
);