## 실행 방법

1. 도커 실행

```
cd docker
docker-compose -p chess up -d
docker exec -it chess_db_1 bash
```

2.mysql 실행

```
mysql -u root -proot
use chess;
```

2. 테이블 정보

```
//main
docker/db/mysql/init/init.sql
//test
resources/schema.sql
```

3. 포트번호

```
http://localhost:8080/
```

4. 테이블 사용

```
use chess;
```

## 요구사항

- Spring Framework를 활용하여 애플리케이션을 구동한다.
- Spark를 대체하여 Spring MVC를 활용하여 요청을 받고 응답을 한다.
- Spring JDBC를 활용하여 DB 접근하던 기존 로직을 대체한다.

## 프로그래밍 요구사항

- [x] 스프링 애플리케이션으로 체스가 실행 가능 해야한다.
- [x] @Controller나 @RestController를 활용하여 요청을 받아야 한다.
- [x] Spring JDBC에서 제공하는 JdbcTemplate를 이용하여 Connection을 직접 만들어 주는 로직을 대체한다.
- [x] JdbcTemplate는 매번 새로 생성하지 않고 빈 주입을 받아서 사용한다.

### 체스방 만들기

- [x] localhost:8080 요청 시 노출되는 페이지에 체스방을 만들 수 있는 버튼이 있다.
- [x] 체스방 만들기 버튼을 누르고 체스방 제목과 비밀번호를 입력하면 새로운 체스판이 만들어진다.
- [x] 체스방에는 고유식별값이 부여된다. (이 고유 식별값은 체스방 주소에서 사용 됨)

### 체스방 목록 조회하기

- [ ] localhost:8080 요청 시 체스방 목록을 조회할 수 있다
- [ ] 체스방 목록에는 체스방 제목과 체스방을 삭제할 수 있는 버튼이 표시된다.

### 체스방 참여하기

- [ ] 체스방 목록에서 체스방 제목을 클릭하면 체스 게임을 이어서 진행할 수 있다.

### 체스방 삭제하기

- [ ] 체스방 목록에서 체스방 삭제 버튼을 클릭하고 체스방 생성시 설정한 비밀번호를 입력하면 체스 게임을 삭제할 수 있다.
- [ ] 진행중인 체스방은 삭제할 수 없다.

## TODO

- [x] 기존의 spark로 구현한 controller를 spring으로 대체한다.
    - [x] ModelAndView를 사용해 웹 페이지를 렌더링한다.
- [x] 기존의 jdbc로 구현한 dao를 Spring jdbcTemplate으로 대체한다.
- [x] 서버 DB(MySQL)와 테스트용 DB(H2)를 따로 만들어 관리한다.

## 웹 기능 목록

- [x] 방 번호를 입력받아 새로운 체스 게임을 시작한다.
    - [x] 게임이 존재하는 경우, 이전 게임에 이어서 게임을 진행한다.
    - 게임이 존재하지 않는 경우, 빈 체스판을 보여준다.

- [x] 게임이 진행되는 동안 마우스 드래그로 체스말을 움직인다.
- [x] 게임 중 exit버튼을 누르면 게임 방은 사라지며, 대기실로 이동한다.
- [x] 게임 중 현재 게임 상태를 확인할 수 있다.
    - [x] 킹이 죽지 않으면 승자는 알 수 없다.
    - [x] 킹이 죽으면 승자를 알 수 있다.
    - [x] 킹이 죽으면 더이상 움직일 수 없다.

- [x] 게임 방 번호별로 동시에 게임을 진행할 수 있다.

## TODO

- [x] test에서 displayname 적기
- [x] pieceDao에서 Bulk insert batchUpdate로 구현 & application.properties변경
- [x] Readme로 실행방법 update.
- [x] moveDto 변경(gameId삭제)
- [x] Private method를 public으로 바꾸기. (컨트롤러)
- [x] 에러 처리 어떻게 할 것인지 고민해보기 -> httpservlet 사용
- [x] 전체적으로 함수이름, 함수위치 컨벤션 수정
- [x] ChessGameService를 bean으로 주입
- [x] JdbcTemplate final로 선언
- [x] default 접근제어자를 private로 변경
- [x] [스프링 bean 주입 방식 공부]()
- [x] 생성자 주입 사용 이유
    - 필드 주입(@Autuwired)의 취약성(더 공부)
- [x] [@Controller 와 @RestController 의 차이 공부](https://velog.io/@betterfuture4/Spring-Controller-RestController-Annotations)
- [x] ResponseBody로 내려주기
- [ ] 패스워드 포함된 form 값을 받아오는 방법 공부하기
- [ ] test 코드 DB schema.sql로 초기화(세팅)

## 질문

- 
