-- DB 생성
CREATE
DATABASE chess DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE
chess;

-- 초기 테이블 생성
CREATE TABLE room
(
    id       INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    black_id INT,
    white_id INT,
    title    VARCHAR(64) NOT NULL,
    status   VARCHAR(64) NOT NULL
);

CREATE TABLE user
(
    id       INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nickname VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL
);

CREATE TABLE log
(
    id             INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    room_id        INT         NOT NULL, -- room_id와 연결
    start_position VARCHAR(12) NOT NULL,
    end_position   VARCHAR(12) NOT NULL,
    register_date  timestamp DEFAULT NOW()
);

CREATE TABLE result
(
    id      INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    game_id INT NOT NULL, -- room_id와 연결
    winner  INT NOT NULL, -- user.id와 연결
    loser   INT NOT NULL  -- user.id와 연결
);

-- 초기 데이터 생성
