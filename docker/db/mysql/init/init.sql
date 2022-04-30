create table chess_game
(
    id      int primary key auto_increment,
    name               varchar(20) not null unique,
    password           varchar(20) not null,
    power              bool        not null,
    team_value_of_turn varchar(20) not null
)

create table board
(
    id              int primary key auto_increment,
    chess_game_id         int not null,
    position_column_value varchar(1)  not null,
    position_row_value    int         not null,
    piece_name            varchar(20) not null,
    piece_team_value      varchar(20) not null,
    FOREIGN KEY (chess_game_id) REFERENCES chess_game(id) on delete cascade
)