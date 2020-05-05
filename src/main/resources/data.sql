INSERT INTO room (room_name) VALUES ('티거');

INSERT INTO state (state, room_id) VALUES ('ended', 1);

INSERT INTO piece (piece_type, team, coordinate, room_id)
VALUES
    ('knight', 'white', 'g1', 1),
    ('rook', 'white', 'h1', 1),
    ('pawn', 'black', 'a7', 1),
    ('pawn', 'white', 'e2', 1),
    ('pawn', 'black', 'c7', 1),
    ('pawn', 'black', 'h7', 1),
    ('bishop', 'white', 'f1', 1),
    ('bishop', 'black', 'f8', 1),
    ('rook', 'black', 'a8', 1),
    ('pawn', 'black', 'e7', 1),
    ('rook', 'black', 'h8', 1),
    ('pawn', 'white', 'c2', 1),
    ('queen', 'white', 'd1', 1),
    ('rook', 'white', 'a1', 1),
    ('pawn', 'black', 'b7', 1),
    ('pawn', 'white', 'b2', 1),
    ('bishop', 'white', 'c1', 1),
    ('pawn', 'white', 'f2', 1),
    ('pawn', 'white', 'a2', 1),
    ('bishop', 'black', 'c8', 1),
    ('queen', 'black', 'd8', 1),
    ('pawn', 'black', 'f7', 1),
    ('knight', 'black', 'b8', 1),
    ('pawn', 'black', 'd7', 1),
    ('pawn', 'white', 'g2', 1),
    ('king', 'black', 'e8', 1),
    ('knight', 'black', 'g8', 1),
    ('pawn', 'black', 'g7', 1),
    ('pawn', 'white', 'h2', 1),
    ('knight', 'white', 'b1', 1),
    ('pawn', 'white', 'd2', 1),
    ('king', 'white', 'e1', 1);

INSERT INTO announcement (message, room_id) VALUES ('게임이 종료되었습니다.<br/>정보를 확인하려면 status, 다시 시작하려면 start를 입력해주세요.', 1);

INSERT INTO status_record (record, game_date, room_name) VALUES ('비겼습니다.<br/>검은 팀의 점수는 38.000000 입니다.<br/>흰 팀의 점수는 38.000000 입니다.', '2020-05-01 17:16:18', '티거');