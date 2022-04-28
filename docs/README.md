# Step 1

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

## DB 실행 방법

1. docker-compose -p chess up -d
2. docker exec -it chess_db_1 bash
3. mysql -u root -proot
4. /docker/db/mysql/init/chess.sql 의 SQL 실행

# Step 2

## 기능 요구사항

### 체스방 만들기
- [x] localhost:8080 요청 시 노출되는 페이지에 체스방을 만들 수 있는 버튼이 있다.
- [x] 체스방 만들기 버튼을 누르고 체스방 제목과 비밀번호를 입력하면 새로운 체스판이 만들어진다.
- [x] 체스방에는 고유식별값이 부여된다. (이 고유 식별값은 체스방 주소에서 사용 됨)
### 체스방 목록 조회하기
- [x] localhost:8080 요청 시 체스방 목록을 조회할 수 있다
- [x] 체스방 목록에는 체스방 제목과 체스방을 삭제할 수 있는 버튼이 표시된다.
### 체스방 참여하기
- [x] 체스방 목록에서 체스방 제목을 클릭하면 체스 게임을 이어서 진행할 수 있다.
### 체스방 삭제하기
- [x] 체스방 목록에서 체스방 삭제 버튼을 클릭하고 체스방 생성시 설정한 비밀번호를 입력하면 체스 게임을 삭제할 수 있다.
- [x] 진행중인 체스방은 삭제할 수 없다.