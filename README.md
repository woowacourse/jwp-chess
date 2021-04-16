# jwp-chess
체스 게임 구현을 위한 저장소

## Commit convention
- 커밋 메시지 언어 : 한글
- feat : 기능 추가.
- refactor : 구조 개선, 변수명 및 메소드명 수정.
- fix : 에러가 나는 부분 해결.
- docs : document 파일 관련.
- test : 테스트 코드만 바꿀 때 사용.
- style : 들여쓰기 수정 및 기타 수정 사항.

## 기능 구현 목록
- [x] Spring MVC 적용
    - [x] 초기 화면 띄워주기 (GET)
    - [x] 시작하기 버튼을 누르면, 체스 보드 판을 띄우기 (GET)
    - [x] 기물을 움직이면, 정상적으로 움직인 후 정보를 반환하여 보여주기 (POST)
- [x] Spring JDBC 적용
    - [x] Spring JDBC에서 제공하는 JdbcTemplate를 이용하여 Connection을 직접 만들어 주는 로직을 대체
        - [x] CREATE
        - [x] READ
        - [x] UPDATE
        - [x] DELETE

## 요구사항
- 요구사항
    - Spring Framework를 활용하여 애플리케이션을 구동한다.
    - Spark를 대체하여 Spring MVC를 활용하여 요청을 받고 응답을 한다.
    - Spring JDBC를 활용하여 DB 접근하던 기존 로직을 대체한다.

- 프로그래밍 요구사항
    - 스프링 애플리케이션으로 체스가 실행 가능 해야한다.
    - @Controller나 @RestController를 활용하여 요청을 받아야 한다.
    - Spring JDBC에서 제공하는 JdbcTemplate를 이용하여 Connection을 직접 만들어 주는 로직을 대체한다.
    - JdbcTemplate는 매번 새로 생성하지 않고 빈 주입을 받아서 사용한다.
    

## DB 테이블 구조
![table_structure](./img/table_structure.png)
```sql
CREATE DATABASE chess_db;

USE chess_db;

CREATE TABLE chess_game (
    current_turn_team VARCHAR(5) NOT NULL,
    is_playing boolean NOT NULL
);

CREATE TABLE team_info (
    team VARCHAR(5) NOT NULL,
    piece_info VARCHAR(400) NOT NULL,
    primary key (team)
);
```       
