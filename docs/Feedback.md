# 웹 체스 피드백

## 1단계

- [x] Controller의 분리 기준
    - 고민 포인트
        - 컨트롤러도 하나의 책임을 가진 객체, 최대한 하나의 컨트롤러가 하나의 책임을 가지고 있는지 고민하여 분리
    - 현재 @Controller, @RestController를 분리한 것은 `비슷한 일을 하는 객체를 분리`하여 가독성이 떨어졌다.
    - Room 관련 Controller는 `SpringChessController`하나로 통합하여 관리
- [x] HTTP Method를 사용하는 기준
    - `Body`를 통해서 데이터를 전달할 것인지, `Query`를 통해서 데이터를 전달할 것인지
- [x] 빈 주입과 관련하여 고민해볼 부분(JdbcTemplate 관련)
    - [x] 빈으로 생성되어야하는 객체는 무엇일까?
        - 스프링에서 관리를 받기위해 빈으로 등록하며 빈으로 등록하여 Singleton으로 만들어 사용
    - [x] 생성한 빈을 주입받는 방법은 어떤게 있을까?
        - 생성자 주입
        - 필드 주입
        - 수정자 주입
- [x] @ControllerAdvice
    - [x] 현재 코드에서 RuntimeException이 아닌 Exception이 발생한다면 사용자에게 미치는 영향
        - RuntimeException 하위 클래스에 대한 예외만 던저줄것이라 생각.
        - Exception으로 발생한 에러까지 포함될 수 있게 Exception을 인자로 받도록 수정
- [x] EOL 경고 해결

