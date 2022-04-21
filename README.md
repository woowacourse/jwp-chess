# java-chess

체스 미션 저장소

## 우아한테크코스 코드리뷰

- [온라인 코드 리뷰 과정](https://github.com/woowacourse/woowacourse-docs/blob/master/maincourse/README.md)

## 1단계

spring MVC 를 사용하여 변경한다.

- [ ] DAO 를 빈으로 등록되게 한다.
  - [ ] BoardDAO
  - [x] GameStatusDAO
  - [x] TurnDAO
- [ ] Service 를 빈으로 등록되게 한다.
  - [ ] ChessGameService
- [ ] Controller 를 빈으로 등록되게 한다.

### DAO
- [ ] JDBC Template을 이용하여 Connection을 직접 만들어 주는 로직을 대체한다.
- [ ] JdbcTemplate는 매번 새로 생성하지 않고 빈 주입을 받아서 사용한다.
- 
### Service
- [ ] @Service 어노테이션을 사용한다.
- 
### Controller
- [ ] @Controller 나 @RestController 를 활용하여 요청한다.
- [ ] 요청과 컨트롤러를 맵핑하기 위해 어노테이션을 사용한다. 
- [ ] 반환 타입은 객체로 보낸다. 




