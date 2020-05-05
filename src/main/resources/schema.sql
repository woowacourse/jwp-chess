create table piece_info(
    id bigint auto_increment primary key,
    room_name_hash varchar(255),
    piece varchar(256) not null,
    position varchar(256) not null
);

create table room_info(
    id bigint auto_increment primary key,
    room_name varchar(255),
    current_turn varchar(255) not null,
    is_game_end boolean not null
);
