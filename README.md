## 스프링 옮기기
1. 뷰 매핑
2. db 연결 / Spring Jdbc
3. 요청 처리 핸들러 정의
4. 에러 페이지 매핑

## 실행 환경
- 기본 포트 : 8080
- 메인 페이지 : "/room/list"
- DB Table
```SQL
CREATE TABLE game_status(
    id int not null auto_increment primary key,
    room_id BIGINT,
    turn  CHAR(10) not null,
    board TEXT
    );
```
```SQL
CREATE TABLE room_status(
    id int not null auto_increment primary key,
    room_name CHAR(10) not null,
    room_id BIGINT
    );
```