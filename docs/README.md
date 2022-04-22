## 1. 기능 구현 목록
- [ ] Spring Framework를 활용하여 애플리케이션을 구동한다.
  - [x] Spark를 대체하여 Spring MVC를 활용하여 요청을 받고 응답을 한다.
  - [x] Spring JDBC를 활용하여 DB 접근하던 기존 로직을 대체한다.
  - [x] html 대신 hbs(handlebars)를 활용한다.

## 2. 프로그래밍 요구사항
- [x] 스프링 애플리케이션으로 체스가 실행 가능 해야한다.
- [x] @Controller나 @RestController를 활용하여 요청을 받아야 한다.
- [x] Spring JDBC에서 제공하는 JdbcTemplate를 이용하여 Connection을 직접 만들어 주는 로직을 대체한다.
- [x] JdbcTemplate는 매번 새로 생성하지 않고 빈 주입을 받아서 사용한다.