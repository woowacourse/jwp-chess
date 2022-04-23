# 웹 체스게임
Spark 기반의 웹 체스게임을 Spring으로 대체하는 프로젝트입니다.

## 🚀 1단계 - Spring 적용하기

### 요구사항
- Spring Framework를 활용하여 애플리케이션을 구동한다.
- Spark를 대체하여 Spring MVC를 활용하여 요청을 받고 응답을 한다.
- Spring JDBC를 활용하여 DB 접근하던 기존 로직을 대체한다.

### 프로그래밍 요구사항
- 스프링 애플리케이션으로 체스가 실행 가능 해야한다.
- @Controller나 @RestController를 활용하여 요청을 받아야 한다.
- Spring JDBC에서 제공하는 JdbcTemplate를 이용하여 Connection을 직접 만들어 주는 로직을 대체한다.
- JdbcTemplate는 매번 새로 생성하지 않고 빈 주입을 받아서 사용한다.

### HTTP API
- `/`
  - get: 체스 게임 초기화면
- `/start`
  - post: 체스판을 초기화한다.
- `/game`
  - get: 체스판을 출력한다.
  - post: 기물을 움직인다.
- `/result`
  - get: 체스판과 점수 결과를 출력한다.
  - post: 게임을 끝내고 결과를 출력할 수 있는 상태로 변경한다.
