create table room
(
    room_id     bigint auto_increment
        primary key,
    room_name   varchar(20)      not null,
    board       char(64)         not null,
    turn        char(5)          not null,
    finish_flag char default 'N' not null
);