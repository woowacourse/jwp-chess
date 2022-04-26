create table chess.game_room
(
    game_room_id varchar(99) primary key,
    name         varchar(20) not null,
    password     varchar(20) not null
);

create table chess.chess_game
(
    game_room_id       varchar(99) not null,
    is_on              bool        not null,
    team_value_of_turn varchar(20) not null,
    foreign key (game_room_id) references chess.game_room (game_room_id) on delete cascade
);

create table chess.board
(
    game_room_id          varchar(99) not null,
    position_column_value varchar(1)  not null,
    position_row_value    int         not null,
    piece_name            varchar(20) not null,
    piece_team_value      varchar(20) not null,
    foreign key (game_room_id) references chess.game_room (game_room_id) on delete cascade
);