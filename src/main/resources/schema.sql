CREATE TABLE IF NOT EXISTS `chess`
(
    `game_id`
    bigint
    NOT
    NULL
    auto_increment,
    `data`
    text
    NOT
    NULL,
    PRIMARY
    KEY
(
    `game_id`
)
    );

CREATE TABLE IF NOT EXISTS `room`
(
    `room_id`
    bigint
    NOT
    NULL
    auto_increment,
    `room_name`
    varchar
(
    100
) NOT NULL,
    `game_id` bigint NOT NULL,
    PRIMARY KEY
(
    `room_id`
),
    FOREIGN KEY
(
    `game_id`
) REFERENCES chess
(
    `game_id`
)
    );
