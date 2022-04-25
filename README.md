# jwp-chess
### 웹 체스 미션 저장소
체스 미션을 바탕으로 Spring Framework 를 적용시킨다.
> [유콩](https://github.com/kyukong/java-chess/tree/step2) 과 [라라](https://github.com/sure-why-not/java-chess/tree/step2) 의 프로젝트 중 라라의 프로젝트를 선택하였다.

## ♟️ 웹 체스 미션 요구사항
- [X] Spring Framework를 활용하여 애플리케이션을 구동한다.
- [X] Spark를 대체하여 Spring MVC를 활용하여 요청을 받고 응답을 한다.
- [X] Spring JDBC를 활용하여 DB 접근하던 기존 로직을 대체한다.

## 👩‍💻 프로그래밍 요구사항
- [X] 스프링 애플리케이션으로 체스가 실행 가능 해야한다.
- [X] @Controller나 @RestController를 활용하여 요청을 받아야 한다.
- [X] Spring JDBC에서 제공하는 JdbcTemplate를 이용하여 Connection을 직접 만들어 주는 로직을 대체한다.
- [X] JdbcTemplate는 매번 새로 생성하지 않고 빈 주입을 받아서 사용한다.
