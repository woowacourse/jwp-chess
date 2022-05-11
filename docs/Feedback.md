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

## 2단계 - 1차 피드백

- [x] `ObjectMapper`를 통해 Gson 라이브러리 의존성 제거
- [x] Controller - status 메서드에서 Body를 Map으로 전달한 이유, DTO로 전달하는 것은 어떤지
    - `StatusDto` - 필드로 board와 score를 Map의 value를 가지도록 구현
- [x] Arguments - 컨트롤러에서 정해주는 것이 적절한가?
    - Command 관련 피드백
- [ ] ExceptionHandler
    - RuntimeException -> Exception으로 바꾼 이유
- [x] DAO에서 Dto가 필요한지
    - [x] BoardDao
    - [x] RoomDao
        - 삭제 시 (게임명, 비밀번호)를 이용하는 것이 아닌 id로 지우도록 수정하고 비밀번호 검증은 service에서 하도록 수정
- [ ] 클래스명에 Spring이라는 이름을 붙인 이유
- [x] SpringBoardDao - `validateExist()`, DAO에서 유효성 검사를 하는 것이 적절한지
- [x] SpringGameDao - `readStateAndColor()`
    - 응답값이 왜 문자열인지
    - 상태와 색 필드를 같는 클래스를 추가해도 되지 않은지?
        - `GameStateDto` - `State`, `Color`를 필드로 가지고 있는 클래스로 수정 후 사용
- [x] 줄바꿈의 기준
    - 잦은 줄바꿈은 코드를 파악할 때 흐름이 끊김.
    - 기존 100자로 설정되어있었던 설정 그대로 사용, 현재 200으로 수정
- [x] ChessRoomService
    - [x] `checkDisabledOption()`
        - 서비스에서 게임 규칙과 관련된 처리를 맡기지말고 도메인에서 처리
        - 문자열보다는 enum을 활용
        - `GameState` 클래스에서 String으로 가지고 있던 state 정보를 `enum`으로 구현
            - enum 내 `disableOption` 필드로 버튼 옵션 정보 포함
    - [x] `validateDuplicateRoomName()`
        - `findByName()`의 결과를 변수로 추출
        - 쿼리 결과를 boolean으로 반환하는 메서드로 만들어보면 어떤지
        - `RoomDao` - `existRoomName()`메서드를 통해 Boolean 반환할 수 있도록 수정
    - [x] `validatePassword()`
        - 유효성 검증이 게임 규칙, 서비스에서 검증하는게 맞는지?
        - `Room` 클래스를 생성하여 비밀번호 검증 로직 포함하도록 작성
- [x] RoomDao
    - 반환값을 null 사용 x -> 검증하기 위한 코드로 복잡도가 올라감.
    - `Optional` 사용

## 2단계 - 2차 피드백

- [x] `Contoller`
    - [x] 메서드 인자로 `body`라는 모호한 변수명은 사용하지 않는다.
        - `요청`의 의미를 담아 `request`와 데이터 타입을 결합하여 사용
    - [x] RequestBody가 String이 아닌 객체가 들어왔을 때 왜 정상 동작하는지 알아보자.
        - RequestBody 사용 시 MessageConverter를 통해 Http request 본문 <-> Java Object의 변환이 가능하다.
    - [x] JsonProcessingException이 필요한가?
        - ObjectMapper.readValue()의 사용을 통해 필요한 Exception이었으나 해당 메서드를 사용하지 않게 되어 제거
- [x] `Field Injection`에 대한 학습
- [ ] `BoardDao` 인터페이스의 필요성
    - [ ] 테스트를 위한 Interface -> 다른 테스트 방법 확인 필요
    - `BoardDao 인터페이스 제거`
- [x] `DAO`에서 불필요한 `DTO`의 사용
    - `BoardDao`에서 `BoardDto`, `PieceDto`, `PointDto`의 사용 제거 및 클래스 제거
- [x] `RoomDAO` - 중복 검증 메서드 코드 수정
    - Optional 활용하여 코드 간소화

## 수정

- [ ] DAO
    - [ ] Room
    - [ ] Game
    - [ ] Board
- [ ] Service
    - [ ] Room
    - [ ] Game
    - [ ] Board
- [ ] Controller
    - [ ] Room
    - [ ] Game
    - [ ] Board
