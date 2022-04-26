drop table board if exists;

drop table chess_game if exists;

drop table game_room if exists;

create table game_room
(
    game_room_id varchar(99) primary key,
    name         varchar(20) not null,
    password     varchar(20) not null
);

create table chess_game
(
    game_room_id       varchar(99) not null,
    is_on              bool        not null,
    team_value_of_turn varchar(20) not null,
    foreign key (game_room_id) references game_room (game_room_id) on delete cascade
);

create table board
(
    game_room_id          varchar(99) not null,
    position_column_value varchar(1)  not null,
    position_row_value    int         not null,
    piece_name            varchar(20) not null,
    piece_team_value      varchar(20) not null,
    foreign key (game_room_id) references game_room (game_room_id) on delete cascade
);

insert into game_room (game_room_id, name, password)
values ('1111', 'game1', '1111');

insert into game_room (game_room_id, name, password)
values ('2222', 'game2', '2222');

insert into chess_game (game_room_id, is_on, team_value_of_turn)
values ('1111', true, 'BLACK');

insert into board (game_room_id, position_column_value, position_row_value, piece_name, piece_team_value)
values ('1111', 'a', 2, 'KING', 'BLACK');

insert into board (game_room_id, position_column_value, position_row_value, piece_name, piece_team_value)
values ('1111', 'b', 3, 'QUEEN', 'WHITE');

insert into board (game_room_id, position_column_value, position_row_value, piece_name, piece_team_value)
values ('2222', 'e', 4, 'PAWN', 'WHITE');