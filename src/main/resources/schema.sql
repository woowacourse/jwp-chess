CREATE TABLE room (
	id INT NOT NULL AUTO_INCREMENT,
    room_name VARCHAR(20) NOT NULL UNIQUE,
    PRIMARY KEY(id)
);

CREATE TABLE state (
	id INT NOT NULL AUTO_INCREMENT,
    state ENUM('ended', 'moved', 'reported', 'started') NOT NULL DEFAULT 'ended',
    room_id INT NOT NULL UNIQUE,
    PRIMARY KEY(id),
    FOREIGN KEY(room_id) REFERENCES room (id) ON DELETE CASCADE
);

CREATE TABLE piece (
	id INT NOT NULL AUTO_INCREMENT,
    piece_type ENUM('bishop', 'king', 'knight', 'pawn', 'queen', 'rook') NOT NULL,
    team ENUM('white', 'black', 'none') NOT NULL,
	coordinate CHAR(2) NOT NULL,
    room_id INT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(room_id) REFERENCES room(id) ON DELETE CASCADE
);

CREATE TABLE announcement (
	id INT NOT NULL AUTO_INCREMENT,
    message VARCHAR(255) NOT NULL,
    room_id INT NOT NULL UNIQUE,
    PRIMARY KEY(id),
    FOREIGN KEY(room_id) REFERENCES room(id) ON DELETE CASCADE
);

CREATE TABLE status_record (
	id INT NOT NULL AUTO_INCREMENT,
    record VARCHAR(255) NOT NULL,
    game_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    room_name VARCHAR(255) NOT NULL,
    PRIMARY KEY(id)
);