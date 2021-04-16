USE test;

CREATE TABLE rooms (
    room_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(12) NOT NULL,
    turn VARCHAR(6) NOT NULL,
    state JSON NOT NULL,
    PRIMARY KEY (room_id),
    UNIQUE KEY (name)
);