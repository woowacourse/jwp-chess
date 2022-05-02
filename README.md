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

## 2단계
### 체스방 만들기
- [x] 루트 경로에 접속하면 체스방을 만들 수 있는 버튼이 있다.
- [x] 체스방 만들기 버튼이 있고, 방을 만들 때는 체스방 제목과 비밀번호를 입력해야 한다.

### 체스방 목록 조회하기
- [x] 루트 경로에 접속하면, 체스방 목록들이 있고 제목과 체스방을 삭제할 수 있는 버튼이 있다.

### 체스방 참여하기
- [x] 체스방 목록에서 제목을 클릭하면 체스 게임을 이어서 진행할 수 있다.
- [x] 체스방 삭제 버튼을 누르고 비밀번호를 입력하면 체스 게임을 삭제할 수 있다. (단, 진행중인 체스방은 삭제할 수 없다.)
