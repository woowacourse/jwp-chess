create table game (
    id bigint not null auto_increment,
    uuid varchar(36) not null,
    name varchar(255) not null,
    can_continue tinyint(1) default 1 not null,
    primary key(id)
);

create table history (
    id bigint not null auto_increment,
    start varchar(2) not null,
    end varchar(2) not null,
    game_id bigint not null,
    primary key(id),
    foreign key(game_id) references game(id)
);