<<<<<<< HEAD
CREATE TABLE game (
      id INT AUTO_INCREMENT NOT NULL,
      name VARCHAR(20) NOT NULL,
      password VARCHAR(20) NOT NULL,
      state VARCHAR(20) NOT NULL,
      PRIMARY KEY (id)
);

CREATE TABLE piece (
       id INT AUTO_INCREMENT NOT NULL,
       position  VARCHAR(10) NOT NULL,
       piece_type VARCHAR(10) NOT NULL,
       color VARCHAR(10) NOT NULL,
       game_id INT NOT NULL,
       PRIMARY KEY (id),
       FOREIGN KEY (game_id) REFERENCES game (id) ON DELETE CASCADE
=======
CREATE TABLE game
(
    game_id int         NOT NULL PRIMARY KEY,
    state   varchar(20) NOT NULL
);

CREATE TABLE piece
(
    piece_id   int         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    game_id    INT         NOT NULL,
    position   VARCHAR(10) NOT NULL,
    piece_type VARCHAR(10) NOT NULL,
    color      VARCHAR(10) NOT NULL,
    FOREIGN KEY (game_id)
        REFERENCES game (game_id) ON DELETE CASCADE
>>>>>>> refactor: GameDao가 JdbcTemplate을 사용하도록 변경
);
