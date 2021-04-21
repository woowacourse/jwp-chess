ALTER DATABASE chess DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS users (
    name varchar(255) NOT NULL PRIMARY KEY,
    win int(11) NOT NULL default 0,
    lose int(11) NOT NULL default 0
);

CREATE TABLE IF NOT EXISTS room (
    id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    is_opened boolean NOT NULL,
    white varchar(255) NOT NULL,
    black varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS play_log (
    id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
    board json NOT NULL,
    game_status json NOT NULL,
    room_id int NOT NULL,
    ast_played_time timestamp default NOW()
);
