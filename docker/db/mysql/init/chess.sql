CREATE TABLE room (
    id int auto_increment NOT NULL,
    name VARCHAR(10) UNIQUE NOT NULL,
    password VARCHAR(12) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE game (
    room_id int NOT NULL,
    turn_color VARCHAR(10) NOT NULL,
    state VARCHAR(10) NOT NULL,
    PRIMARY KEY (room_id),
    FOREIGN KEY(room_id) REFERENCES room (id)
);

CREATE TABLE board (
    piece_type VARCHAR(10) NOT NULL,
    piece_color VARCHAR(10) NOT NULL,
    vertical_index INT NOT NULL,
    horizontal_index INT NOT NULL,
    room_id int NOT NULL,
    FOREIGN KEY(room_id) REFERENCES game (room_id)
 );

