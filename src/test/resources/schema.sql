CREATE TABLE game (
      id INT AUTO_INCREMENT NOT NULL,
      name VARCHAR(20) NOT NULL,
      password VARCHAR(20) NOT NULL,
      state VARCHAR(20) NOT NULL,
      PRIMARY KEY (id)
);

CREATE TABLE piece (
    id         INT AUTO_INCREMENT NOT NULL,
    position   VARCHAR(10) NOT NULL,
    piece_type VARCHAR(10) NOT NULL,
    color      VARCHAR(10) NOT NULL,
    game_id    INT         NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (game_id) REFERENCES game (id) ON DELETE CASCADE
);
