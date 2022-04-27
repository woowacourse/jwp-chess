use chess;

CREATE TABLE command (
    id INT NOT NULL,
    command VARCHAR(20) NOT NULL
);

CREATE TABLE rooms (
    id INT NOT NULL,
    name VARCHAR(20),
    password VARCHAR(32) NOT NULL
);