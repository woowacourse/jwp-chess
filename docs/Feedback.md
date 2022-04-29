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

## 2단계

- [ ] `ObjectMapper`를 통해 Gson 라이브러리 의존성 제거
- [ ] Controller - status 메서드에서 Body를 Map으로 전달한 이유, DTO로 전달하는 것은 어떤지
- [ ] Arguments - 컨트롤러에서 정해주는 것이 적절한가?
    - Command 관련 피드백
- [ ] ExceptionHandler
    - RuntimeException -> Exception으로 바꾼 이유
- [ ] BoardDao - DAO에서 Dto가 필요한지
- [ ] 클래스명에 Spring이라는 이름을 붙인 이유
- [ ] SpringBoardDao - `validateExist()`, DAO에서 유효성 검사를 하는 것이 적절한지
- [ ] SpringGameDao - `readStateAndColor()`
    - 응답값이 왜 문자열인지
    - 상태와 색 필드를 같는 클래스를 추가해도 되지 않은지?
- [ ] 줄바꿈의 기준
    - 잦은 줄바꿈은 코드를 파악할 때 흐름이 끊김.
- [ ] ChessRoomService
    - [ ] `checkDisabledOption()`
        - 서비스에서 게임 규칙과 관련된 처리를 맡기지말고 도메인에서 처리
        - 문자열보다는 enum을 활용
    - [ ] `validateDuplicateRommName()`
        - `findByName()`의 결과를 변수로 추출
        - 쿼리 결과를 boolean으로 반환하는 메서드로 만들어보면 어떤지
    - [ ] `validatePassword()`
        - 유효성 검증이 게임 규칙, 서비스에서 검증하는게 맞는지?
- [ ] RoomDao
    - 반환값을 null 사용 x -> 검증하기 위한 코드로 복잡도가 올라감.
