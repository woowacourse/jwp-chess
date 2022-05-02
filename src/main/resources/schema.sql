CREATE TABLE game (
                      game_id INT PRIMARY KEY auto_increment,
                      current_turn VARCHAR(10) DEFAULT'WHITE'
);

CREATE TABLE piece (
                       piece_id INT PRIMARY KEY AUTO_INCREMENT,
                       game_id INT NOT NULL,
                       piece_name VARCHAR(10) NOT NULL,
                       piece_color VARCHAR(10) NOT NULL,
                       position VARCHAR(2) NOT NULL,
                       FOREIGN KEY (game_id) REFERENCES game(game_id)
);

CREATE TABLE room
(
    room_id       int primary key auto_increment,
    game_id       int         not null,
    room_name     varchar(10) not null,
    room_password varchar(10) not null,
    status varchar(4) default'STOP',
    foreign key (game_id) references game (game_id)
);
