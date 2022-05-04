insert into Player (id, color, pieces) values
    (1, 'White', 'a1:King'),
    (2, 'Black', 'a2:King');

insert into Game (id, title, password, player_id1, player_id2, finished, turn_color) values
    (1, 'title', 'password', 1, 2, 0, 'White');