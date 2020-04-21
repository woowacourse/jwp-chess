# java-chess
체스 게임 구현을 위한 저장소

## 1단계 기능 목록

- [x] 체스판을 초기화 할 수 있다.
- [x] 말 이동을 할 수 있다.
- [x] 승패 및 점수를 판단할 수 있다.
- [x] 웹으로 체스 게임이 가능해야 한다.(스파크 적용)
- [x] 웹 서버를 재시작하더라도 이전에 하던 체스 게임을 다시 시작할 수 있어야 한다.(DB 적용)

## Spark Chess 게임 실행법

1. Docker 로 mysql 서버 실행하기 (docker 필요)
- 터미널 : src/main/resources/sql 로 이동
- 터미널 : docker-compose up -d; 명령어로 서버시작

2. DB 세팅하기 (쿼리문 작성)
- 터미널 : docker exec -it local-chess-db bash;
- 터미널 : mysql -u root -p; (비밀번호 : root) 로 mysql 접속
- src/main/resources/sql 내의 sql 문 보고 쿼리문 작성.

3. 실행
- SparkChessApplication main 실행
- localhost:4567 로 접속