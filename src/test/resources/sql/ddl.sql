set mode MySQL;

DROP TABLE rooms IF EXISTS;

CREATE TABLE rooms
(
    room_id SERIAL,
    name    VARCHAR(12) UNIQUE,
    turn    VARCHAR(6),
    state   CLOB
);

