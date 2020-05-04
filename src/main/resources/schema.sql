CREATE TABLE if not exists game_room
(
    id    bigint AUTO_INCREMENT primary key,
    name  varchar(255) NOT NULL unique,
    state boolean      NOT NULL
);

CREATE TABLE if not exists game_history
(
    id              bigint AUTO_INCREMENT primary key,
    game_room       bigint     NOT NULL,
    source_position varchar(5) NOT NULL,
    target_position varchar(5) NOT NULL,
    created_time    timestamp  NOT NULL
);