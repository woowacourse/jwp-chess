# 웹 체스 피드백

## 1단계 
- [ ] Controller의 분리 기준
  - 고민 포인트 
    - 컨트롤러도 하나의 책임을 가진 객체, 최대한 하나의 컨트롤러가 하나의 책임을 가지고 있는지 고민하여 분리
  - 현재 @Controller, @RestController를 분리한 것은 `비슷한 일을 하는 객체를 분리`하여 가독성이 떨어졌다.
- [ ] HTTP Method를 사용하는 기준 
- [ ] 빈 주입과 관련하여 고민해볼 부분(JdbcTemplate 관련)
  - [ ] 빈으로 생성되어야하는 객체는 무엇일까?
  - [ ] 생성한 빈을 주입받는 방법은 어떤게 있을까?
- [ ] @ControllerAdvice
  - [ ] 현재 코드에서 RuntimeException이 아닌 Exception이 발생한다면 사용자에게 미치는 영향
- [x] EOL 경고 해결

