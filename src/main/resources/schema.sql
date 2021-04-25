-- DB 생성
CREATE
DATABASE chess DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE
chess;

-- 초기 테이블 생성
CREATE TABLE player
(
    id       INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nickname VARCHAR(64) NOT NULL,
    password VARCHAR(10) NOT NULL
);

CREATE TABLE result
(
    id      INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    game_id INT NOT NULL, -- room_id와 연결
    winner  INT NOT NULL, -- user.id와 연결
    loser   INT NOT NULL  -- user.id와 연결
);

CREATE TABLE room
(
    id         INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(64) NOT NULL,
    black_user INT,                -- user.id와 연결
    white_user INT,                -- user.id와 연결
    status     TINYINT(4) NOT NULL -- 0 -> 종료됨 / 1 -> 진행중 / 2 -> 준비중
);

CREATE TABLE history
(
    id             INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    room_id        INT         NOT NULL, -- room_id와 연결
    start_position VARCHAR(12) NOT NULL,
    end_position   VARCHAR(12) NOT NULL,
    register_date  timestamp DEFAULT NOW()
);
