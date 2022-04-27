# 🚀 1단계 - Spring 적용하기

## 실행 방법

```shell
## 도커 실행
docker exec -it chess_db_1 bash

## MySQL 접속
mysql -u root -proot
use chess;

## init
DROP TABLE IF EXISTS piece;
DROP TABLE IF EXISTS chess_game;

CREATE TABLE chess_game
(
    id            INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(10) NOT NULL,
    status        VARCHAR(10) NOT NULL,
    current_color CHAR(5)     NOT NULL,
    black_score   VARCHAR(10) NOT NULL,
    white_score   VARCHAR(10) NOT NULL
);

CREATE TABLE piece
(
    position      CHAR(2)     NOT NULL,
    chess_game_id INT         NOT NULL,
    color         CHAR(5)     NOT NULL,
    type          VARCHAR(10) NOT NULL,
    PRIMARY KEY (position, chess_game_id),
    FOREIGN KEY (chess_game_id) REFERENCES chess_game (id)
);
```

SpringChessApplication 실행후 localhost:8080 접속
