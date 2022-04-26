DROP TABLE board IF EXISTS;
DROP TABLE game IF EXISTS;

CREATE TABLE game (
                      id BIGINT AUTO_INCREMENT,
                      room_name VARCHAR(100) NOT NULL UNIQUE,
                      password VARCHAR(100) NOT NULL,
                      turn_color VARCHAR(10) NOT NULL,
                      state VARCHAR(10) NOT NULL,
                      PRIMARY KEY (id)
);

CREATE TABLE board (
                       piece_type VARCHAR(10) NOT NULL,
                       piece_color VARCHAR(10) NOT NULL,
                       vertical_index INT NOT NULL,
                       horizontal_index INT NOT NULL,
                       game_id BIGINT NOT NULL,
                       FOREIGN KEY(game_id) REFERENCES game (id)
);
