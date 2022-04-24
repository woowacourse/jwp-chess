create table Member
(
    id   bigint auto_increment primary key,
    name varchar(10) not null
);

create table Game
(
    id bigint auto_increment primary key,
    turn varchar(10) not null,
    white_member_id bigint,
    black_member_id bigint
);

create table Piece
(
    game_id bigint not null,
    square_file varchar(2) not null,
    square_rank varchar(2) not null,
    team varchar(10),
    piece_type varchar(10) not null,
    constraint fk_game_id foreign key(game_id) references Game(id) on delete cascade
);
