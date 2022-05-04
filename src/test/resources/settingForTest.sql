delete from board;
delete from chess_game;
delete from game_room;

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