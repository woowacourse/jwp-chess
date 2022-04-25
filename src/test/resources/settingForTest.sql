drop table board if exists;

drop table chess_game if exists;

create table chess_game
(
    chess_game_id      int primary key auto_increment,
    name               varchar(20) not null,
    is_on              bool        not null,
    team_value_of_turn varchar(20) not null
);


create table board
(
    chess_game_id         int         not null,
    position_column_value varchar(1)  not null,
    position_row_value    int         not null,
    piece_name            varchar(20) not null,
    piece_team_value      varchar(20) not null,
    foreign key (chess_game_id) references chess_game (chess_game_id) on delete cascade
);

insert into chess_game (name, is_on, team_value_of_turn)
values ('test', true, 'BLACK');

insert into board(chess_game_id, position_column_value, position_row_value, piece_name, piece_team_value)
values (1, 'a', 2, 'KING', 'BLACK');

insert into board(chess_game_id, position_column_value, position_row_value, piece_name, piece_team_value)
values (1, 'b', 3, 'QUEEN', 'WHITE');