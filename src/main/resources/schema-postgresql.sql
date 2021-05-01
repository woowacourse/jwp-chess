CREATE TABLE IF NOT EXISTS game
(
    id
    SERIAL
    PRIMARY
    KEY,
    gameid
    INT,
    name
    VARCHAR
(
    25
),
    color VARCHAR
(
    25
),
    position VARCHAR
(
    25
)
    );

CREATE TABLE IF NOT EXISTS current_color
(
    id
    SERIAL
    PRIMARY
    KEY,
    game_id
    INT,
    color
    VARCHAR
(
    25
)
    );