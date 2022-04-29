# 1단계 - Spring 적용하기 

## 1. 기능 구현 목록
- [x] Spring Framework를 활용하여 애플리케이션을 구동한다.
  - [x] Spark를 대체하여 Spring MVC를 활용하여 요청을 받고 응답을 한다.
  - [x] Spring JDBC를 활용하여 DB 접근하던 기존 로직을 대체한다.
  - [x] html 대신 hbs(handlebars)를 활용한다.

## 2. 프로그래밍 요구사항
- [x] 스프링 애플리케이션으로 체스가 실행 가능 해야한다.
- [x] @Controller나 @RestController를 활용하여 요청을 받아야 한다.
- [x] Spring JDBC에서 제공하는 JdbcTemplate를 이용하여 Connection을 직접 만들어 주는 로직을 대체한다.
- [x] JdbcTemplate는 매번 새로 생성하지 않고 빈 주입을 받아서 사용한다.

# 2단계 - 동시에 여러 게임 하기

## 1. 기능 구현 목록
- [X] 체스 게임을 진행할 수 있는 방을 반들어서 동시에 여러 게임이 가능하도록 구현
- [x] 체스방 만들기
  - [x] localhost:8080 -> 노출되는 페이지에 체스방 생성 버튼
  - [x] 체스방은 제목과 비밀번호를 통해 생성
  - [x] 각각의 체스방은 고유 식별값이 존재(체스방 주소에 사용)
- [x] 체스방 목록 조회하기
  - [x] localhost:8080 -> 체스방 목록 출력
  - [x] 각각의 체스방은 제목과 삭제 버튼 존재
- [x] 체스방 참여하기
  - [x] 체스방 제목 클릭 시 게임을 이어서 진행 가능 
- [x] 체스방 삭제하기
  - [x] 삭제 버튼 클릭 후 비밀번호 입력 시 체스 게임 삭제 
  - [x] **진행 중인 체스 방은 삭제 불가**
  
## 2. 프로그래맹 요구사항 
- [x] 예외 발생 시 사용자가 이해할 수 있는 명시적인 메세지 응답

## REST API
- 게임방 목록 화면
  - GET : "/"
- 게임방 생성
  - POST : "/rooms"
- 게임방 입장
  - GET : "/rooms/{roomId}"
- 게임방 삭제
  - DELETE : "/rooms/{roomId}"
- 게임 시작
  - PUT : “/rooms/{roomId}/start”
- 게임 종료
  - PUT : “/rooms/{roomId}/end”
- 게임 점수 계산
  - POST : “/rooms/{roomId}/status”
- 기물 이동 
  - PUT : “/rooms/{roomId}/move”
- 게임 나가기 
  - GET : "/"

## DB 실행 방법

1. docker-compose -p chess up -d
2. docker exec -it chess_db_1 bash
3. mysql -u root -proot
4. /docker/db/mysql/init/chess.sql 의 SQL 실행
