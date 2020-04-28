create table chess_game
(
    id   bigint auto_increment,
    team varchar(5) not null
);

create table piece
(
    id    bigint auto_increment,
    piece varchar(6) not null
);

create table position
(
    id       bigint auto_increment,
    position char(2) not null
);

create table team
(
    team varchar(5) not null
);

create table board
(
    position_id bigint not null,
    piece_id    bigint not null,
    team_id     bigint not null,
    game_id     bigint not null
);

INSERT INTO position
values (1, 'A1');
INSERT INTO position
values (2, 'A2');
INSERT INTO position
values (3, 'A3');
INSERT INTO position
values (4, 'A4');
INSERT INTO position
values (5, 'A5');
INSERT INTO position
values (6, 'A6');
INSERT INTO position
values (7, 'A7');
INSERT INTO position
values (8, 'A8');
INSERT INTO position
values (9, 'B1');
INSERT INTO position
values (10, 'B2');
INSERT INTO position
values (11, 'B3');
INSERT INTO position
values (12, 'B4');
INSERT INTO position
values (13, 'B5');
INSERT INTO position
values (14, 'B6');
INSERT INTO position
values (15, 'B7');
INSERT INTO position
values (16, 'B8');
INSERT INTO position
values (17, 'C1');
INSERT INTO position
values (18, 'C2');
INSERT INTO position
values (19, 'C3');
INSERT INTO position
values (20, 'C4');
INSERT INTO position
values (21, 'C5');
INSERT INTO position
values (22, 'C6');
INSERT INTO position
values (23, 'C7');
INSERT INTO position
values (24, 'C8');
INSERT INTO position
values (25, 'D1');
INSERT INTO position
values (26, 'D2');
INSERT INTO position
values (27, 'D3');
INSERT INTO position
values (28, 'D4');
INSERT INTO position
values (29, 'D5');
INSERT INTO position
values (30, 'D6');
INSERT INTO position
values (31, 'D7');
INSERT INTO position
values (32, 'D8');
INSERT INTO position
values (33, 'E1');
INSERT INTO position
values (34, 'E2');
INSERT INTO position
values (35, 'E3');
INSERT INTO position
values (36, 'E4');
INSERT INTO position
values (37, 'E5');
INSERT INTO position
values (38, 'E6');
INSERT INTO position
values (39, 'E7');
INSERT INTO position
values (40, 'E8');
INSERT INTO position
values (41, 'F1');
INSERT INTO position
values (42, 'F2');
INSERT INTO position
values (43, 'F3');
INSERT INTO position
values (44, 'F4');
INSERT INTO position
values (45, 'F5');
INSERT INTO position
values (46, 'F6');
INSERT INTO position
values (47, 'F7');
INSERT INTO position
values (48, 'F8');
INSERT INTO position
values (49, 'G1');
INSERT INTO position
values (50, 'G2');
INSERT INTO position
values (51, 'G3');
INSERT INTO position
values (52, 'G4');
INSERT INTO position
values (53, 'G5');
INSERT INTO position
values (54, 'G6');
INSERT INTO position
values (55, 'G7');
INSERT INTO position
values (56, 'G8');
INSERT INTO position
values (57, 'H1');
INSERT INTO position
values (58, 'H2');
INSERT INTO position
values (59, 'H3');
INSERT INTO position
values (60, 'H4');
INSERT INTO position
values (61, 'H5');
INSERT INTO position
values (62, 'H6');
INSERT INTO position
values (63, 'H7');
INSERT INTO position
values (64, 'H8');

INSERT INTO piece
values (1, 'PAWN');
INSERT INTO piece
values (2, 'KNIGHT');
INSERT INTO piece
values (3, 'BISHOP');
INSERT INTO piece
values (4, 'ROOK');
INSERT INTO piece
values (5, 'QUEEN');
INSERT INTO piece
values (6, 'KING');

INSERT INTO team
values ('WHITE');
INSERT INTO team
values ('BLACK');
