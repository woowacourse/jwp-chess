# java-chess

체스 미션 저장소

## 우아한테크코스 코드리뷰

- [온라인 코드 리뷰 과정](https://github.com/woowacourse/woowacourse-docs/blob/master/maincourse/README.md)

## 1단계

spring MVC 를 사용하여 변경한다.

- [x] DAO 를 빈으로 등록되게 한다.
  - [x] BoardDAO
  - [x] GameStatusDAO
  - [x] TurnDAO
- [x] Service 를 빈으로 등록되게 한다.
  - [x] ChessGameService
- [x] Controller 를 빈으로 등록되게 한다.

### DAO
- [x] JDBC Template을 이용하여 Connection을 직접 만들어 주는 로직을 대체한다.
- [x] JdbcTemplate는 매번 새로 생성하지 않고 빈 주입을 받아서 사용한다.

### Service
- [x] @Service 어노테이션을 사용한다.

### Controller
- [x] @Controller 나 @RestController 를 활용하여 요청한다.
- [x] 요청과 컨트롤러를 맵핑하기 위해 어노테이션을 사용한다. 
- [x] 반환 타입은 객체로 보낸다. 


<hr/>

## docker로 사용하실 때 변경해야할 것!!

```properties
spring.h2.console.enabled=false
#spring.h2.console.path=/h2-console

#spring.datasource.url=jdbc:h2:mem:testdb
#spring.datasource.driverClassName=org.h2.Driver
#spring.datasource.username=sa
#spring.datasource.password=

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/example?serverTimezone=UTC&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=1234
```

application.properties 위 내용으로 변경에서 사용하면 될 것 같아요!!!
