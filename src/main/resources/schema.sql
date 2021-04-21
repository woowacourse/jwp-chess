CREATE TABLE rooms(
    id          int(11) NOT NULL AUTO_INCREMENT,
    name        varchar(255) NOT NULL,
    state       varchar(45)  NOT NULL,
    currentteam varchar(45)  NOT NULL,
    created_at  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE pieces
(
    id        int(11) NOT NULL AUTO_INCREMENT,
    roomid    int(11) NOT NULL,
    signature varchar(45) NOT NULL,
    team      varchar(45) NOT NULL,
    location  varchar(45) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (roomid) REFERENCES rooms (id)
);