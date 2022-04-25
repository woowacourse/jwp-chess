
## 실행 방법

1. 도커 실행

  ```
  cd docker
  docker-compose -p chess up -d
  ```
2. 테이블 정보
  ```
docker/db/mysql/init/init.sql
   ```
3. 포트번호
 ```
http://localhost:8080/
   ```

## 요구사항

- Spring Framework를 활용하여 애플리케이션을 구동한다.
- Spark를 대체하여 Spring MVC를 활용하여 요청을 받고 응답을 한다.
- Spring JDBC를 활용하여 DB 접근하던 기존 로직을 대체한다.

## 프로그래밍 요구사항

- 스프링 애플리케이션으로 체스가 실행 가능 해야한다.
- @Controller나 @RestController를 활용하여 요청을 받아야 한다.
- Spring JDBC에서 제공하는 JdbcTemplate를 이용하여 Connection을 직접 만들어 주는 로직을 대체한다.
- JdbcTemplate는 매번 새로 생성하지 않고 빈 주입을 받아서 사용한다.

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
