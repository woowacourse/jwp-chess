drop table if exists piece cascade;
drop table if exists chess_game cascade;

CREATE TABLE chess_game
(
    id       bigint                                   NOT NULL AUTO_INCREMENT,
    turn     enum ('WHITE_TURN', 'BLACK_TURN', 'END') NOT NULL,
    title    varchar(10)                              NOT NULL,
    password varchar(50)                              NOT NULL,
    primary key (id),
    unique key title_unique_key (title)
);

CREATE TABLE piece
(
    id            bigint                  NOT NULL AUTO_INCREMENT,
    type          varchar(10)             NOT NULL,
    color         enum ('WHITE', 'BLACK') NOT NULL,
    position_col  varchar(1)              NOT NULL,
    position_row  varchar(1)              NOT NULL,
    chess_game_id bigint                  NOT NULL,
    primary key (id),
    foreign key (chess_game_id) references chess_game (id) on delete cascade
);
