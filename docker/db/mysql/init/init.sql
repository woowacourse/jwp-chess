CREATE TABLE pieces
(
    id    int NOT NULL AUTO_INCREMENT,
    type  ENUM('k','q','b','n','r','p','.'),
    color ENUM('BLACK','WHITE','NONE'),
    PRIMARY KEY (id)
);

CREATE TABLE positions
(
    id int NOT NULL AUTO_INCREMENT,
    x  int NOT NULL,
    y  int NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE boards
(
    id          int NOT NULL AUTO_INCREMENT,
    position_id int,
    piece_id    int,
    PRIMARY KEY (id),
    FOREIGN KEY (position_id) REFERENCES positions (id),
    FOREIGN KEY (piece_id) REFERENCES pieces (id)
);

CREATE TABLE games
(
    id    int NOT NULL AUTO_INCREMENT,
    state ENUM('READY','PLAY','FINISH'),
    turn  ENUM('BLACK','WHITE'),
    PRIMARY KEY (id)
);

INSERT INTO positions (x,y) VALUES (0,0);
INSERT INTO positions (x,y) VALUES (0,1);
INSERT INTO positions (x,y) VALUES (0,2);
INSERT INTO positions (x,y) VALUES (0,3);
INSERT INTO positions (x,y) VALUES (0,4);
INSERT INTO positions (x,y) VALUES (0,5);
INSERT INTO positions (x,y) VALUES (0,6);
INSERT INTO positions (x,y) VALUES (0,7);

INSERT INTO positions (x,y) VALUES (1,0);
INSERT INTO positions (x,y) VALUES (1,1);
INSERT INTO positions (x,y) VALUES (1,2);
INSERT INTO positions (x,y) VALUES (1,3);
INSERT INTO positions (x,y) VALUES (1,4);
INSERT INTO positions (x,y) VALUES (1,5);
INSERT INTO positions (x,y) VALUES (1,6);
INSERT INTO positions (x,y) VALUES (1,7);

INSERT INTO positions (x,y) VALUES (2,0);
INSERT INTO positions (x,y) VALUES (2,1);
INSERT INTO positions (x,y) VALUES (2,2);
INSERT INTO positions (x,y) VALUES (2,3);
INSERT INTO positions (x,y) VALUES (2,4);
INSERT INTO positions (x,y) VALUES (2,5);
INSERT INTO positions (x,y) VALUES (2,6);
INSERT INTO positions (x,y) VALUES (2,7);

INSERT INTO positions (x,y) VALUES (3,0);
INSERT INTO positions (x,y) VALUES (3,1);
INSERT INTO positions (x,y) VALUES (3,2);
INSERT INTO positions (x,y) VALUES (3,3);
INSERT INTO positions (x,y) VALUES (3,4);
INSERT INTO positions (x,y) VALUES (3,5);
INSERT INTO positions (x,y) VALUES (3,6);
INSERT INTO positions (x,y) VALUES (3,7);

INSERT INTO positions (x,y) VALUES (4,0);
INSERT INTO positions (x,y) VALUES (4,1);
INSERT INTO positions (x,y) VALUES (4,2);
INSERT INTO positions (x,y) VALUES (4,3);
INSERT INTO positions (x,y) VALUES (4,4);
INSERT INTO positions (x,y) VALUES (4,5);
INSERT INTO positions (x,y) VALUES (4,6);
INSERT INTO positions (x,y) VALUES (4,7);

INSERT INTO positions (x,y) VALUES (5,0);
INSERT INTO positions (x,y) VALUES (5,1);
INSERT INTO positions (x,y) VALUES (5,2);
INSERT INTO positions (x,y) VALUES (5,3);
INSERT INTO positions (x,y) VALUES (5,4);
INSERT INTO positions (x,y) VALUES (5,5);
INSERT INTO positions (x,y) VALUES (5,6);
INSERT INTO positions (x,y) VALUES (5,7);

INSERT INTO positions (x,y) VALUES (6,0);
INSERT INTO positions (x,y) VALUES (6,1);
INSERT INTO positions (x,y) VALUES (6,2);
INSERT INTO positions (x,y) VALUES (6,3);
INSERT INTO positions (x,y) VALUES (6,4);
INSERT INTO positions (x,y) VALUES (6,5);
INSERT INTO positions (x,y) VALUES (6,6);
INSERT INTO positions (x,y) VALUES (6,7);

INSERT INTO positions (x,y) VALUES (7,0);
INSERT INTO positions (x,y) VALUES (7,1);
INSERT INTO positions (x,y) VALUES (7,2);
INSERT INTO positions (x,y) VALUES (7,3);
INSERT INTO positions (x,y) VALUES (7,4);
INSERT INTO positions (x,y) VALUES (7,5);
INSERT INTO positions (x,y) VALUES (7,6);
INSERT INTO positions (x,y) VALUES (7,7);

INSERT INTO pieces (type,color) VALUES ("k","WHITE");
INSERT INTO pieces (type,color) VALUES ("k","BLACK");

INSERT INTO pieces (type,color) VALUES ("q","WHITE");
INSERT INTO pieces (type,color) VALUES ("q","BLACK");

INSERT INTO pieces (type,color) VALUES ("b","WHITE");
INSERT INTO pieces (type,color) VALUES ("b","BLACK");

INSERT INTO pieces (type,color) VALUES ("n","WHITE");
INSERT INTO pieces (type,color) VALUES ("n","BLACK");

INSERT INTO pieces (type,color) VALUES ("r","WHITE");
INSERT INTO pieces (type,color) VALUES ("r","BLACK");

INSERT INTO pieces (type,color) VALUES ("p","WHITE");
INSERT INTO pieces (type,color) VALUES ("p","BLACK");

INSERT INTO pieces (type,color) VALUES (".","NONE");