## 스프링 옮기기
1. 뷰 매핑
2. db 연결 / Spring Jdbc
3. 요청 처리 핸들러 정의
4. 에러 페이지 매핑

## 실행 환경
- 기본 포트 : 8080
- 메인 페이지 : "/room/list"
- DB Table (DATABASE : web_chess)
```SQL
CREATE TABLE game(
    id bigint not null auto_increment primary key,
    room_id bigint,
    turn  CHAR(10) not null,
    board TEXT
    );
```
```SQL
CREATE TABLE room(
    id bigint not null auto_increment primary key,
    room_name CHAR(10) not null
    );
```