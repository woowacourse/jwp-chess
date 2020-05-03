create table board(
    id bigint auto_increment primary key,
    piece varchar(256) not null,
    position varchar(256) not null
);

create table game_status(
    id bigint auto_increment primary key,
    current_turn varchar(255) not null
);
