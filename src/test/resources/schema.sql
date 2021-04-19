CREATE TABLE chess_game
(id IDENTITY,
state VARCHAR(64) NOT NULL,
PRIMARY KEY (id));

CREATE TABLE piece
(id IDENTITY,
color VARCHAR(255) NOT NULL,
shape VARCHAR(255) NOT NULL,
chess_game_id BIGINT,
row_index INT NOT NULL,
col_index INT NOT NULL,
FOREIGN KEY(chess_game_id) REFERENCES chess_game(id), PRIMARY KEY (id));
