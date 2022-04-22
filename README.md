# 페리포터(페퍼&포키)의 미션 목표🧚

### 공통 목표
- 쉬는시간 적절히 가지기
- 구현은 토요일까지 끝내기 도전...!
    - 퀄리티는 리팩토링으로 올리고, 요구사항 만족에 집중!
- 컨트롤러 / 서비스 역할 분리
- 토론 시간 아까워하지 말기
- Spring 테스트 코드
    - 페퍼 : 익숙해지기
    - 포키 : 경험하기

### 페퍼
- 포키의 칭찬 감옥 극복하기
    - 칭찬에 대한 대답은 “포항항 감사합니다”로 통일하고 변명하지 않기

### 포키
- 경험을 경험으로만 끝내지 말고 이론 학습까지 이어가기
- 페퍼의 마음..빼.앗.기? 가져버리기? 무튼 그런거~
- 페퍼가 스스로의 장점을 더 크게 느끼도록 서포트해주고 싶어요

---

# 미션 요구사항

- Spring Framework를 활용하여 애플리케이션을 구동한다.
- Spark를 대체하여 Spring MVC를 활용하여 요청을 받고 응답을 한다.
- Spring JDBC를 활용하여 DB 접근하던 기존 로직을 대체한다.

## 프로그래밍 요구사항

- 스프링 애플리케이션으로 체스가 실행 가능 해야한다.
- @Controller나 @RestController를 활용하여 요청을 받아야 한다.
- Spring JDBC에서 제공하는 JdbcTemplate를 이용하여 Connection을 직접 만들어 주는 로직을 대체한다.
- JdbcTemplate는 매번 새로 생성하지 않고 빈 주입을 받아서 사용한다.

# Todo

- [ ]  Application 계층에 Spring Framework 적용
- [ ]  Controller 계층에 Spring Framework 적용
- [ ]  DB 계층에 Spring JdbcTemplate 적용
- [ ]  Controller와 Service 분리, 적절하게 네이밍 변경
