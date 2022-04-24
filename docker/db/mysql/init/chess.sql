CREATE TABLE game (
    room_name VARCHAR(10) NOT NULL,
    turn_color VARCHAR(10) NOT NULL,
    state VARCHAR(10) NOT NULL,
    PRIMARY KEY (room_name)
);

CREATE TABLE board (
    piece_type VARCHAR(10) NOT NULL,
    piece_color VARCHAR(10) NOT NULL,
    vertical_index INT NOT NULL,
    horizontal_index INT NOT NULL,
    room_name VARCHAR(10) NOT NULL,
    FOREIGN KEY(room_name) REFERENCES game (room_name)
 );
