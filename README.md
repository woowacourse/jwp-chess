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

## API 명세
- __POST: /games__
    - 새로운 게임방을 만들기
- __GET: /games__ 
    - 만들어진 게임방의 목록을 반환하기
- __POST: /games/:id/new__ 
    - 지정한 게임방에 새로운 체스 게임을 생성하기
- __GET: /games/:id/saved__ 
    - 지정한 게임방에 저장된 체스 게임을 불러오기
- __POST: /games/:id/move__
    - 지정한 체스게임에 한 수를 진행하기

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
- [x] 게임방 생성/조회/삭제 구현
    - [x] POST 요청을 통해 새로운 게임방을 만들기 
        - [x] 방 제목이 중복되면 에러를 출력한다
        - [x] 사용자에게 생성된 방의 id값을 반환하기
    - [x] GET 요청을 통해 게임방의 목록을 가져오기
    - [X] DELETE 요청을 통해 지정한 게임방을 삭제하기
- [x] 지정한 게임방에서 게임을 플레이하도록 구현
    - [x] 게임방 번호를 저장해두기
    - [x] 지정한 게임방 번호로 URL 요청보내기
- [x] 회원가입 기능 구현
    - [x] 만약 id가 중복된다면, 에러를 출력한다
- [x] 로그인 기능 구현
    - [x] 로그인이 되었다면, 세션을 발행한다
    - [x] 세션이 발행된 유저에 한해서만 체스게임방 생성/조인을 가능하도록 한다

## 요구사항
- 요구사항
    - Spring Framework를 활용하여 애플리케이션을 구동한다.
    - Spark를 대체하여 Spring MVC를 활용하여 요청을 받고 응답을 한다.
    - Spring JDBC를 활용하여 DB 접근하던 기존 로직을 대체한다.
    - 체스 게임을 진행할 수 있는 방을 만들어서 동시에 여러 게임이 가능하도록 하기

- 프로그래밍 요구사항
    - 스프링 애플리케이션으로 체스가 실행 가능 해야한다.
    - @Controller나 @RestController를 활용하여 요청을 받아야 한다.
    - Spring JDBC에서 제공하는 JdbcTemplate를 이용하여 Connection을 직접 만들어 주는 로직을 대체한다.
    - JdbcTemplate는 매번 새로 생성하지 않고 빈 주입을 받아서 사용한다.
    
## 리팩토링 중점 사안
- [x] REST API 설계 원칙에 맞게 URI, HTTP method 변경하기
- [x] produce, consumes 에서 JSON 타입 명시가 꼭 필요한지 확인해보기
- [x] @ControllerAdvice 적용해보기
- [x] 생성자 주입을 통해 받은 필드는 final 처리하기
- [x] entrySet을 활용한 리팩토링
- [x] DAO/Service 테스트 케이스 작성
- [x] DAO를 가볍게 유지하기
- [x] DTO 생성 로직을 DTO 내부로 이동시키기
- [x] 테스트 코드 작성

## 질문 사항
- DTO가 Domain을 넘겨받아 생성하는 로직 정도는 가지고 있어도 괜찮을까?
    - Domain을 알고있는 것은 아닐까?
    - DTO의 역할이 Layer간 필요한 데이터 이동이라면, 필요한 데이터 추출 정도는 책임으로 가져도 자연스럽지 않나?
- DAO가 최소한의 DB 로직을 가진다?
    - ORM을 통한 DB 접근은 객체를 반환한다는데, 이건 Repository의 이야기인가?
    - Repository와 Dao는 뭐가 다른걸까?

## DB 테이블 구조
![table_structure](./img/table_structure.png)
```sql
DROP DATABASE if exists chess_db;
CREATE DATABASE chess_db;

USE chess_db;

DROP TABLE IF EXISTS game_room_info CASCADE;
DROP TABLE IF EXISTS chess_game_info CASCADE;
DROP TABLE IF EXISTS team_info CASCADE;

CREATE TABLE game_room_info (
    room_id int NOT NULL AUTO_INCREMENT,
    room_name VARCHAR(50),
    PRIMARY KEY (room_id)
);

CREATE TABLE chess_game_info (
    room_id int NOT NULL,
    current_turn_team VARCHAR(5) NOT NULL,
    is_playing boolean NOT NULL,
    FOREIGN KEY (room_id) REFERENCES game_room_info(room_id)
);

CREATE TABLE team_info (
    room_id int NOT NULL,
    team VARCHAR(5) NOT NULL,
    piece_info VARCHAR(400) NOT NULL,
    FOREIGN KEY (room_id) REFERENCES game_room_info(room_id)
);
```       
