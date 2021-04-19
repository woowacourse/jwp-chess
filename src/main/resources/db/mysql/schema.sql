DROP TABLE IF EXISTS movement;
DROP TABLE IF EXISTS chess;

CREATE TABLE IF NOT EXISTS chess (
    chess_id VARCHAR(36) NOT NULL,
    name VARCHAR(64) NOT NULL,
    winner_color VARCHAR(64) NOT NULL,
    is_running BOOLEAN NOT NULL DEFAULT FALSE,
    created_date TIMESTAMP(6),
    PRIMARY KEY (chess_id)
);


CREATE TABLE IF NOT EXISTS movement (
    movement_id VARCHAR(36) NOT NULL,
    chess_id VARCHAR(36) NOT NULL,
    source_position VARCHAR(64) NOT NULL,
    target_position VARCHAR(64) NOT NULL,
    created_date TIMESTAMP(6),
    PRIMARY KEY (movement_id),
    FOREIGN KEY (chess_id)
    REFERENCES chess (chess_id)
    ON DELETE CASCADE ON UPDATE CASCADE
 );