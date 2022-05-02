create table game
(
    id       int         NOT NULL AUTO_INCREMENT,
    title    varchar(20) NOT NULL,
    password varchar(20) NOT NULL,
    PRIMARY KEY (id)
);

create table board
(
    id       int         NOT NULL AUTO_INCREMENT,
    position varchar(10) not null,
    piece    varchar(20) not null,
    game_id  int         not null,
    primary key (id),
    foreign key (game_id) references game (id)
        on delete cascade
);

create table game_status
(
    id      int         NOT NULL AUTO_INCREMENT,
    status  varchar(10) not null,
    game_id int         not null,
    primary key (id),
    foreign key (game_id) references game (id)
        on delete cascade
);

create table turn
(
    id      int         NOT NULL AUTO_INCREMENT,
    team    varchar(10) not null,
    game_id int         not null,
    primary key (id),
    foreign key (game_id) references game (id)
        on delete cascade
);
